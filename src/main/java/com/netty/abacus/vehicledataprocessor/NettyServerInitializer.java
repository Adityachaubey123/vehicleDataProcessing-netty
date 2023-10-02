package com.netty.abacus.vehicledataprocessor;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.stereotype.Component;
import com.netty.abacus.vehicledataprocessor.eventhandler.DataProcessorHandler;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class NettyServerInitializer {

    private final int port = 1010; // Change this to your desired port
    private static final String CSV_FILE_PATH = "data.csv";

    public void startServer() throws InterruptedException, ParseException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new JsonObjectDecoder());
                            pipeline.addLast(new DataProcessorHandler()); // Implement this handler
                        }
                    });

            // Bind to your desired IP address and port
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            Channel channel = channelFuture.channel();

            // Read CSV data and send it to the DataProcessorHandler
            readCSVDataAndSendToHandler(CSV_FILE_PATH);

            // Wait until the server socket is closed
            channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private void readCSVDataAndSendToHandler(String csvFilePath) throws ParseException {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; 
                    continue;
                }
                
                String[] columns = line.split(",");
                if (columns.length == 6) {
                    String device_id = columns[0].trim();
                    String latitude = columns[1].trim();
                    String longitude = columns[2].trim();
                    String vehicle_speed = columns[3].trim();
                    String door_status = columns[4].trim();
                    String timestampStr = columns[5].trim();

                    LocalDateTime timestamp = LocalDateTime.parse(timestampStr, DateTimeFormatter.ISO_DATE_TIME);

                    String jsonData = String.format(
                            "{\"device_id\": \"%s\", \"latitude\": \"%s\", \"longitude\": \"%s\", \"vehicle_speed\": \"%s\", \"door_status\": \"%s\", \"timestamp\": \"%s\"}",
                            device_id, latitude, longitude, vehicle_speed, door_status, timestamp.toString());

                    DataProcessorHandler dataProcessorHandler = new DataProcessorHandler();
                    dataProcessorHandler.processJsonData(jsonData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
