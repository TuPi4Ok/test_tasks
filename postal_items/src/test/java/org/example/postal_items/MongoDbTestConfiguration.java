package org.example.postal_items;

import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.process.runtime.Network;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import de.flapdoodle.embed.mongo.distribution.Version;
import java.io.IOException;

@Configuration
public class MongoDbTestConfiguration {
    private static final String IP = "localhost";
    private static final int PORT = 28017;
    @Bean
    public IMongodConfig embeddedMongoConfiguration() throws IOException {
        return new MongodConfigBuilder()
                .version(Version.V2_5_1)
                .net(new Net(IP, PORT, Network.localhostIsIPv6()))
                .build();
    }
}
