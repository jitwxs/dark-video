package jit.wxs.dv.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import jit.wxs.dv.domain.entity.DvContent;
import jit.wxs.dv.domain.entity.DvContentAffix;
import jit.wxs.dv.domain.entity.ESContent;
import jit.wxs.dv.domain.enums.ResultEnum;
import jit.wxs.dv.domain.vo.ResultVO;
import jit.wxs.dv.mapper.ESMapper;
import jit.wxs.dv.service.DvContentAffixService;
import jit.wxs.dv.service.DvContentService;
import jit.wxs.dv.service.ESService;
import jit.wxs.dv.service.ThumbnailService;
import jit.wxs.dv.util.ResultVOUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.FastDateFormat;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ElasticSearch Service
 * 内容索引：content 类型：default
 * @author jitwxs
 * @since 2018/10/9 21:34
 */
@Slf4j
@Service
public class ESServiceImpl implements ESService {
    @Autowired
    private ESMapper esMapper;
    @Autowired
    private TransportClient transportClient;
    @Autowired
    private DvContentService contentService;
    @Autowired
    private DvContentAffixService contentAffixService;
    @Autowired
    private ThumbnailService thumbnailService;

    @Override
    public boolean hasContentIndexExist() {
        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest("content");
        IndicesExistsResponse inExistsResponse = transportClient.admin().indices().exists(inExistsRequest).actionGet();

        return inExistsResponse.isExists();
    }

    @Override
    public ResultVO createContentIndex() {
        if(hasContentIndexExist()) {
            return ResultVOUtils.error(ResultEnum.CONTENT_INDEX_HAS_EXIST);
        }

        CreateIndexRequest request = new CreateIndexRequest("content")
                .source("{\n" +
                        "  \"settings\": {\n" +
                        "    \"number_of_shards\": 5,\n" +
                        "    \"number_of_replicas\": 1,\n" +
                        "    \"analysis\": {\n" +
                        "      \"analyzer\": {\n" +
                        "        \"ik\": {\n" +
                        "          \"tokenizer\": \"ik_smart\"\n" +
                        "        }\n" +
                        "      }\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"mappings\": {\n" +
                        "    \"default\": {\n" +
                        "      \"properties\": {\n" +
                        "        \"name\": {\n" +
                        "          \"type\": \"text\"\n" +
                        "        },\n" +
                        "        \"type\": {\n" +
                        "          \"type\": \"integer\"\n" +
                        "        },\n" +
                        "        \"thumbnail\": {\n" +
                        "          \"type\": \"keyword\"\n" +
                        "        },\n" +
                        "        \"create_date\": {\n" +
                        "          \"type\": \"date\",\n" +
                        "          \"format\": \"yyyy-MM-dd HH:mm:ss || yyyy-MM-dd\"\n" +
                        "        }\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }\n" +
                        "}", XContentType.JSON);
        try {
            transportClient.admin().indices().create(request).get();
            return ResultVOUtils.success();
        } catch (Exception e) {
            log.error("创建索引失败,异常为：" + e);
            return ResultVOUtils.error(ResultEnum.CONTENT_INDEX_CREATE_ERROR);
        }
    }

    @Override
    public ResultVO buildContentIndex() {
        // 清空索引
        cleanContentIndex();

        // 建立索引
        int count = 0;
        for(DvContent content : contentService.listAll()) {
            String createDate = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(content.getCreateDate());

            Map<String, Object> jsonMap = new HashMap<>(16);
            jsonMap.put("name", content.getName());
            jsonMap.put("thumbnail", content.getThumbnail());
            jsonMap.put("type", 0);

            jsonMap.put("create_date", createDate);

            IndexResponse response1 = transportClient.prepareIndex("content", "default", content.getId())
                    .setSource(jsonMap)
                    .get();

            if(response1.status() == RestStatus.CREATED) {
                count++;
            }

            if("dir".equals(content.getType())) {
                for(DvContentAffix affix : contentAffixService.listByContentId(content.getId())) {
                    Map<String, Object> jsonMap1 = new HashMap<>(16);
                    jsonMap1.put("name", affix.getName());
                    jsonMap1.put("thumbnail", affix.getThumbnail());
                    jsonMap1.put("type", 1);
                    jsonMap1.put("create_date", createDate);

                    IndexResponse response2 = transportClient.prepareIndex("content", "default", affix.getId())
                            .setSource(jsonMap1)
                            .get();

                    if(response2.status() == RestStatus.CREATED) {
                        count++;
                    }
                }
            }
        }

        return ResultVOUtils.success(count);
    }

    @Override
    public long cleanContentIndex() {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchAllQuery());
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(transportClient)
                .filter(queryBuilder)
                .source("content")
                .get();
        return response.getDeleted();
    }

    @Override
    public Page<ESContent> searchContent(String name, int current, int size) {
        int from = (current - 1) * size;
        Page<ESContent> page = new Page<>(current, size, "create_date", false);

        try {
            SearchResponse response = transportClient.prepareSearch("content")
                    .setTypes("default")
                    .setQuery(QueryBuilders.matchQuery( "name", name))
                    .highlighter(new HighlightBuilder()
                            .field("name")
                            .preTags("<span style='color:red;'>")
                            .postTags("</span>")
                    )
                    .addSort("create_date", SortOrder.DESC)
                    .setFrom(from)
                    .setSize(size)
                    .execute()
                    .get();

            List<ESContent> records = new ArrayList<>(size + 1);
            for(SearchHit hit: response.getHits()){
                Map<String, Object> map = hit.getSourceAsMap();
                ESContent esContent = new ESContent();
                esContent.setId(hit.getId());
                esContent.setType((Integer)map.get("type"));
                esContent.setCreateDate((String) map.get("create_date"));
                esContent.setThumbnail(thumbnailService.getUrl((String) map.get("thumbnail")));

                HighlightField highlightField = hit.getHighlightFields().get("name");
                esContent.setName(highlightField.getFragments()[0].toString());

                if(esContent.getType() == 1) {
                    esContent.setParent(esMapper.getParentESContent(esContent.getId()));
                }

                records.add(esContent);
            }

            page.setTotal((int)response.getHits().totalHits);
            page.setRecords(records);
        } catch (Exception e) {
            log.error("查询失败，错误原因：", e);
        }

        return page;
    }


}
