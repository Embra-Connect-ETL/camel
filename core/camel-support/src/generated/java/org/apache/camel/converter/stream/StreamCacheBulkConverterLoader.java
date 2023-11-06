/* Generated by camel build tools - do NOT edit this file! */
package org.apache.camel.converter.stream;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.DeferredContextBinding;
import org.apache.camel.Exchange;
import org.apache.camel.Ordered;
import org.apache.camel.TypeConversionException;
import org.apache.camel.TypeConverterLoaderException;
import org.apache.camel.TypeConverter;
import org.apache.camel.spi.TypeConvertible;
import org.apache.camel.spi.BulkTypeConverters;
import org.apache.camel.spi.TypeConverterLoader;
import org.apache.camel.spi.TypeConverterRegistry;

/**
 * Generated by camel build tools - do NOT edit this file!
 */
@SuppressWarnings("unchecked")
@DeferredContextBinding
public final class StreamCacheBulkConverterLoader implements TypeConverterLoader, BulkTypeConverters, CamelContextAware {

    private CamelContext camelContext;

    public StreamCacheBulkConverterLoader() {
    }

    @Override
    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @Override
    public CamelContext getCamelContext() {
        return camelContext;
    }

    @Override
    public int size() {
        return 6;
    }

    @Override
    public void load(TypeConverterRegistry registry) throws TypeConverterLoaderException {
        registry.addBulkTypeConverters(this);
        doRegistration(registry);
    }

    @Override
    public <T> T convertTo(Class<?> from, Class<T> to, Exchange exchange, Object value) throws TypeConversionException {
        try {
            Object obj = doConvertTo(from, to, exchange, value);
            if (obj == Void.class) {;
                return null;
            } else {
                return (T) obj;
            }
        } catch (TypeConversionException e) {
            throw e;
        } catch (Exception e) {
            throw new TypeConversionException(value, to, e);
        }
    }

    private Object doConvertTo(Class<?> from, Class<?> to, Exchange exchange, Object value) throws Exception {
        if (to == byte[].class) {
            if (value instanceof org.apache.camel.StreamCache) {
                return org.apache.camel.converter.stream.StreamCacheConverter.convertToByteArray((org.apache.camel.StreamCache) value, exchange);
            }
        } else if (to == java.nio.ByteBuffer.class) {
            if (value instanceof org.apache.camel.StreamCache) {
                return org.apache.camel.converter.stream.StreamCacheConverter.convertToByteBuffer((org.apache.camel.StreamCache) value, exchange);
            }
        } else if (to == org.apache.camel.StreamCache.class) {
            if (value instanceof java.io.ByteArrayInputStream) {
                return org.apache.camel.converter.stream.StreamCacheConverter.convertToStreamCache((java.io.ByteArrayInputStream) value, exchange);
            }
            if (value instanceof java.io.InputStream) {
                return org.apache.camel.converter.stream.StreamCacheConverter.convertToStreamCache((java.io.InputStream) value, exchange);
            }
            if (value instanceof org.apache.camel.converter.stream.CachedOutputStream) {
                return org.apache.camel.converter.stream.StreamCacheConverter.convertToStreamCache((org.apache.camel.converter.stream.CachedOutputStream) value, exchange);
            }
            if (value instanceof java.io.Reader) {
                return org.apache.camel.converter.stream.StreamCacheConverter.convertToStreamCache((java.io.Reader) value, exchange);
            }
        }
        return null;
    }

    private void doRegistration(TypeConverterRegistry registry) {
        registry.addConverter(new TypeConvertible<>(org.apache.camel.StreamCache.class, byte[].class), this);
        registry.addConverter(new TypeConvertible<>(org.apache.camel.StreamCache.class, java.nio.ByteBuffer.class), this);
        registry.addConverter(new TypeConvertible<>(java.io.ByteArrayInputStream.class, org.apache.camel.StreamCache.class), this);
        registry.addConverter(new TypeConvertible<>(java.io.InputStream.class, org.apache.camel.StreamCache.class), this);
        registry.addConverter(new TypeConvertible<>(org.apache.camel.converter.stream.CachedOutputStream.class, org.apache.camel.StreamCache.class), this);
        registry.addConverter(new TypeConvertible<>(java.io.Reader.class, org.apache.camel.StreamCache.class), this);
        
        
    }

    public TypeConverter lookup(Class<?> to, Class<?> from) {
        if (to == byte[].class) {
            if (from == org.apache.camel.StreamCache.class) {
                return this;
            }
        } else if (to == java.nio.ByteBuffer.class) {
            if (from == org.apache.camel.StreamCache.class) {
                return this;
            }
        } else if (to == org.apache.camel.StreamCache.class) {
            if (from == java.io.ByteArrayInputStream.class) {
                return this;
            }
            if (from == java.io.InputStream.class) {
                return this;
            }
            if (from == org.apache.camel.converter.stream.CachedOutputStream.class) {
                return this;
            }
            if (from == java.io.Reader.class) {
                return this;
            }
        }
        return null;
    }

}
