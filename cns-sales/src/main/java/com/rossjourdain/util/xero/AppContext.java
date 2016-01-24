package com.rossjourdain.util.xero;

import org.springframework.context.ApplicationContext;

public final class AppContext {
    
    private static ApplicationContext ctx;
    
    private AppContext() {}
    
    public static void setApplicationContext(ApplicationContext applicationContext){
        ctx = applicationContext;
    }
    
    public static Object getBeanFromContext(String beanName){
        return ctx.getBean(beanName);
    }

	public static ApplicationContext getCtx() {
		return ctx;
	}

	public static void setCtx(ApplicationContext ctx) {
		AppContext.ctx = ctx;
	}
    
}
