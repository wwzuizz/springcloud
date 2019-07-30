package com.linwen.comm.fastjson;

//@Configuration
public class MvcConfig {

//	@Bean
//	public HttpMessageConverters fastJsonHttpMessageConverters() {
//		MappingJackson2HttpMessageConverter fastConverter = new MappingJackson2HttpMessageConverter();
//		FastJsonConfig fastJsonConfig = new FastJsonConfig();
//		fastJsonConfig.setSerializerFeatures(
//				// 输出空置字段
//				SerializerFeature.WriteMapNullValue,
//				// list字段如果为null，输出为[]，而不是null
//				SerializerFeature.WriteNullListAsEmpty,
//				// 数值字段如果为null，输出为0，而不是null
//				SerializerFeature.WriteNullNumberAsZero,
//				// Boolean字段如果为null，输出为false，而不是null
//				SerializerFeature.WriteNullBooleanAsFalse,
//				// 字符类型字段如果为null，输出为""，而不是null
//				SerializerFeature.WriteNullStringAsEmpty);
//
//		fastJsonConfig.setCharset(Charset.forName("UTF-8"));
//		fastJsonConfig.setDateFormat("yyyy-MM-dd hh:mm:ss");
////		fastConverter.setFastJsonConfig(fastJsonConfig);
//		HttpMessageConverter<?> converter = fastConverter;
//		ObjectMapper objectMapper = new ObjectMapper();
//
//		fastConverter.setObjectMapper(objectMapper);
//		return new HttpMessageConverters(converter);
//	}
}