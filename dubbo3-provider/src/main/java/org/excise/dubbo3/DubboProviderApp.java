package org.excise.dubbo3;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.excise.dubbo3.api.DemoService;

import java.util.HashMap;

/**
 * Hello world!
 *
 */
public class DubboProviderApp
{
    public static void main( String[] args )
    {
        startWithBootstrap();
    }

    private static void startWithBootstrap() {
        ServiceConfig<DemoServiceImpl> demoService = new ServiceConfig<>();
        demoService.setInterface(DemoService.class);
        demoService.setRef(new DemoServiceImpl());

        RegistryConfig registryConfig = new RegistryConfig("nacos://123.249.38.62:8848");
        HashMap<String, String> params = new HashMap<>();
        params.put("namespace", "8dea8c26-6e9b-4d1b-baf7-6230a654d74e");
        registryConfig.setParameters(params);
        // registryConfig.setUseAsMetadataCenter(false);
        registryConfig.setUseAsConfigCenter(false);
        registryConfig.setUseAsMetadataCenter(false);

        DubboBootstrap bootstrap = DubboBootstrap.getInstance();

        ApplicationConfig applicationConfig = new ApplicationConfig("DemoService-provider");
        // all 双注册 instance 应用级注册 interface 接口级注册
        applicationConfig.setRegisterMode(CommonConstants.INSTANCE_REGISTER_MODE);

        bootstrap.application(applicationConfig)
                .registry(registryConfig)
                .protocol(new ProtocolConfig(CommonConstants.TRIPLE, -1))
                .service(demoService)
                .start()
                .await();
    }
}
