/* Generated by camel build tools - do NOT edit this file! */
package org.apache.camel.component.micrometer.prometheus;

import javax.annotation.processing.Generated;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.spi.ExtendedPropertyConfigurerGetter;
import org.apache.camel.spi.PropertyConfigurerGetter;
import org.apache.camel.spi.ConfigurerStrategy;
import org.apache.camel.spi.GeneratedPropertyConfigurer;
import org.apache.camel.util.CaseInsensitiveMap;
import org.apache.camel.component.micrometer.prometheus.MicrometerPrometheus;

/**
 * Generated by camel build tools - do NOT edit this file!
 */
@Generated("org.apache.camel.maven.packaging.GenerateConfigurerMojo")
@SuppressWarnings("unchecked")
public class MicrometerPrometheusConfigurer extends org.apache.camel.support.component.PropertyConfigurerSupport implements GeneratedPropertyConfigurer, PropertyConfigurerGetter {

    @Override
    public boolean configure(CamelContext camelContext, Object obj, String name, Object value, boolean ignoreCase) {
        org.apache.camel.component.micrometer.prometheus.MicrometerPrometheus target = (org.apache.camel.component.micrometer.prometheus.MicrometerPrometheus) obj;
        switch (ignoreCase ? name.toLowerCase() : name) {
        case "binders": target.setBinders(property(camelContext, java.lang.String.class, value)); return true;
        case "camelcontext":
        case "camelContext": target.setCamelContext(property(camelContext, org.apache.camel.CamelContext.class, value)); return true;
        case "clearonreload":
        case "clearOnReload": target.setClearOnReload(property(camelContext, boolean.class, value)); return true;
        case "enableexchangeeventnotifier":
        case "enableExchangeEventNotifier": target.setEnableExchangeEventNotifier(property(camelContext, boolean.class, value)); return true;
        case "enableinstrumentedthreadpoolfactory":
        case "enableInstrumentedThreadPoolFactory": target.setEnableInstrumentedThreadPoolFactory(property(camelContext, boolean.class, value)); return true;
        case "enablemessagehistory":
        case "enableMessageHistory": target.setEnableMessageHistory(property(camelContext, boolean.class, value)); return true;
        case "enablerouteeventnotifier":
        case "enableRouteEventNotifier": target.setEnableRouteEventNotifier(property(camelContext, boolean.class, value)); return true;
        case "enableroutepolicy":
        case "enableRoutePolicy": target.setEnableRoutePolicy(property(camelContext, boolean.class, value)); return true;
        case "namingstrategy":
        case "namingStrategy": target.setNamingStrategy(property(camelContext, java.lang.String.class, value)); return true;
        case "path": target.setPath(property(camelContext, java.lang.String.class, value)); return true;
        case "routepolicylevel":
        case "routePolicyLevel": target.setRoutePolicyLevel(property(camelContext, java.lang.String.class, value)); return true;
        case "textformatversion":
        case "textFormatVersion": target.setTextFormatVersion(property(camelContext, java.lang.String.class, value)); return true;
        default: return false;
        }
    }

    @Override
    public Class<?> getOptionType(String name, boolean ignoreCase) {
        switch (ignoreCase ? name.toLowerCase() : name) {
        case "binders": return java.lang.String.class;
        case "camelcontext":
        case "camelContext": return org.apache.camel.CamelContext.class;
        case "clearonreload":
        case "clearOnReload": return boolean.class;
        case "enableexchangeeventnotifier":
        case "enableExchangeEventNotifier": return boolean.class;
        case "enableinstrumentedthreadpoolfactory":
        case "enableInstrumentedThreadPoolFactory": return boolean.class;
        case "enablemessagehistory":
        case "enableMessageHistory": return boolean.class;
        case "enablerouteeventnotifier":
        case "enableRouteEventNotifier": return boolean.class;
        case "enableroutepolicy":
        case "enableRoutePolicy": return boolean.class;
        case "namingstrategy":
        case "namingStrategy": return java.lang.String.class;
        case "path": return java.lang.String.class;
        case "routepolicylevel":
        case "routePolicyLevel": return java.lang.String.class;
        case "textformatversion":
        case "textFormatVersion": return java.lang.String.class;
        default: return null;
        }
    }

    @Override
    public Object getOptionValue(Object obj, String name, boolean ignoreCase) {
        org.apache.camel.component.micrometer.prometheus.MicrometerPrometheus target = (org.apache.camel.component.micrometer.prometheus.MicrometerPrometheus) obj;
        switch (ignoreCase ? name.toLowerCase() : name) {
        case "binders": return target.getBinders();
        case "camelcontext":
        case "camelContext": return target.getCamelContext();
        case "clearonreload":
        case "clearOnReload": return target.isClearOnReload();
        case "enableexchangeeventnotifier":
        case "enableExchangeEventNotifier": return target.isEnableExchangeEventNotifier();
        case "enableinstrumentedthreadpoolfactory":
        case "enableInstrumentedThreadPoolFactory": return target.isEnableInstrumentedThreadPoolFactory();
        case "enablemessagehistory":
        case "enableMessageHistory": return target.isEnableMessageHistory();
        case "enablerouteeventnotifier":
        case "enableRouteEventNotifier": return target.isEnableRouteEventNotifier();
        case "enableroutepolicy":
        case "enableRoutePolicy": return target.isEnableRoutePolicy();
        case "namingstrategy":
        case "namingStrategy": return target.getNamingStrategy();
        case "path": return target.getPath();
        case "routepolicylevel":
        case "routePolicyLevel": return target.getRoutePolicyLevel();
        case "textformatversion":
        case "textFormatVersion": return target.getTextFormatVersion();
        default: return null;
        }
    }
}

