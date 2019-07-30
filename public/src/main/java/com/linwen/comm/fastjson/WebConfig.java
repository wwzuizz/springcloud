package com.linwen.comm.fastjson;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

//@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
//    @Bean
//    FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
//        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
//        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//        converter.setFastJsonConfig(fastJsonConfig);
//        return converter;
//    }
//    /**
//     此处实现WebMvcConfigurer接口，重写extendMessageConverters方法，
//     使其StringHttpMessageConverter在fastJsonHttpMessageConverter之前进行执行，
//     可以有效避免String字符的重复序列化导致的斜杠问题
//     */
//    @Override
//    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//    /*
//        需注意，converters中默认还包含很多的HttpMessageConverter，如：ByteArrayHttpMessageConverter等很多 <br/>
//        此处使用converters.clear();将会导致所有的转换器全部清空，尽管在下面代码中有重新添加两个转换器，<br/>
//        但如果你的程序中需要使用到其它转换器，请记得在此处重新add一下； kbase-psrt
//     */
//        converters.clear();
//        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
//        converters.add(converter);
//        converters.add(fastJsonHttpMessageConverter());
//    }
}