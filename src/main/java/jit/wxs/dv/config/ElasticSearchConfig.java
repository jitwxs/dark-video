package jit.wxs.dv.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;

/**
 * ElasticSearch配置
 * @author jitwxs
 * @since 2018/10/9 21:31
 */
@Configuration
public class ElasticSearchConfig {
    private Logger logger  = LoggerFactory.getLogger(this.getClass());

    @Value("${elasticsearch.node.ip}")
    private String ip;
    @Value("${elasticsearch.node.port}")
    private String port;

    @Bean
    public TransportClient getTransportClient() {
        logger.info("ElasticSearch初始化开始。。");
        logger.info("要连接的节点的ip是{}，端口是{}" , ip , port);
        TransportClient transportClient = null;
        try {
            Settings settings = Settings.builder()
                    .put("client.transport.sniff", true)
                    .build();
            transportClient = new PreBuiltTransportClient(settings);
            transportClient.addTransportAddress(new TransportAddress(InetAddress.getByName(ip),Integer.parseInt(port)));
            logger.info("ElasticSearch初始化完成。。");
        }catch (Exception e){
            e.printStackTrace();
            logger.error("ElasticSearch初始化失败：" +  e.getMessage(),e);
        }
        return transportClient;
    }
}
