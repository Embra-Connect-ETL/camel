/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.model;

import java.util.List;

import jakarta.xml.bind.JAXBException;

import org.apache.camel.model.language.ExpressionDefinition;
import org.apache.camel.model.loadbalancer.FailoverLoadBalancerDefinition;
import org.apache.camel.model.loadbalancer.RandomLoadBalancerDefinition;
import org.apache.camel.model.loadbalancer.RoundRobinLoadBalancerDefinition;
import org.apache.camel.model.loadbalancer.StickyLoadBalancerDefinition;
import org.apache.camel.model.loadbalancer.TopicLoadBalancerDefinition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class XmlParseTest extends XmlTestSupport {

    @Test
    public void testParseSimpleRouteXml() throws Exception {
        RouteDefinition route = assertOneRoute("simpleRoute.xml");
        assertFrom(route, "seda:a");
        assertChildTo("to", route, "seda:b");
    }

    @Test
    public void testParseProcessorXml() throws Exception {
        RouteDefinition route = assertOneRoute("processor.xml");
        assertFrom(route, "seda:a");
        ProcessDefinition to = assertOneProcessorInstanceOf(ProcessDefinition.class, route);
        assertEquals("myProcessor", to.getRef(), "Processor ref");
    }

    @Test
    public void testParseProcessorWithFilterXml() throws Exception {
        RouteDefinition route = assertOneRoute("processorWithFilter.xml");
        assertFrom(route, "seda:a");
        FilterDefinition filter = assertOneProcessorInstanceOf(FilterDefinition.class, route);
        assertExpression(filter.getExpression(), "juel", "in.header.foo == 'bar'");
    }

    @Test
    public void testParseProcessorWithHeaderFilterXml() throws Exception {
        RouteDefinition route = assertOneRoute("processorWithHeaderFilter.xml");
        assertFrom(route, "seda:a");
        FilterDefinition filter = assertOneProcessorInstanceOf(FilterDefinition.class, route);
        assertExpression(filter.getExpression(), "header", "foo");
    }

    @Test
    public void testParseProcessorWithSimpleFilterXml() throws Exception {
        RouteDefinition route = assertOneRoute("processorWithSimpleFilter.xml");
        assertFrom(route, "seda:a");
        FilterDefinition filter = assertOneProcessorInstanceOf(FilterDefinition.class, route);
        assertExpression(filter.getExpression(), "simple", "${in.header.foo} == 'bar'");
    }

    @Test
    public void testParseProcessorWithGroovyFilterXml() throws Exception {
        RouteDefinition route = assertOneRoute("processorWithGroovyFilter.xml");
        assertFrom(route, "seda:a");
        FilterDefinition filter = assertOneProcessorInstanceOf(FilterDefinition.class, route);
        assertExpression(filter.getExpression(), "groovy", "in.headers.any { h -> h.startsWith('foo')}");
    }

    @Test
    public void testParseRecipientListXml() throws Exception {
        RouteDefinition route = assertOneRoute("dynamicRecipientList.xml");
        assertFrom(route, "seda:a");
        RecipientListDefinition<?> node = assertOneProcessorInstanceOf(RecipientListDefinition.class, route);
        assertExpression(node.getExpression(), "header", "foo");
    }

    @Test
    public void testParseStaticRecipientListXml() throws Exception {
        RouteDefinition route = assertOneRoute("staticRecipientList.xml");
        assertFrom(route, "seda:a");
        assertChildTo(route, "seda:b", "seda:c", "seda:d");
    }

    @Test
    public void testParseTransformXml() throws Exception {
        RouteDefinition route = assertOneRoute("transform.xml");
        assertFrom(route, "direct:start");
        TransformDefinition node = assertNthProcessorInstanceOf(TransformDefinition.class, route, 0);
        assertExpression(node.getExpression(), "simple", "${in.body} extra data!");
        assertChildTo(route, "mock:end", 1);
    }

    @Test
    public void testParseSagaXml() throws Exception {
        RouteDefinition route = assertOneRoute("saga.xml");
        assertFrom(route, "direct:start");
        SagaDefinition node = assertNthProcessorInstanceOf(SagaDefinition.class, route, 0);
        assertNotNull(node.getCompensation());
        assertChildTo(route, "mock:end", 2);
    }

    @Test
    public void testParseScriptXml() throws Exception {
        RouteDefinition route = assertOneRoute("script.xml");
        assertFrom(route, "direct:start");
        ScriptDefinition node = assertNthProcessorInstanceOf(ScriptDefinition.class, route, 0);
        assertExpression(node.getExpression(), "groovy", "System.out.println(\"groovy was here\")");
        assertChildTo(route, "mock:end", 1);
    }

    @Test
    public void testParseSetBodyXml() throws Exception {
        RouteDefinition route = assertOneRoute("setBody.xml");
        assertFrom(route, "direct:start");
        SetBodyDefinition node = assertNthProcessorInstanceOf(SetBodyDefinition.class, route, 0);
        assertExpression(node.getExpression(), "simple", "${in.body} extra data!");
        assertChildTo(route, "mock:end", 1);
    }

    @Test
    public void testParseSetHeaderXml() throws Exception {
        RouteDefinition route = assertOneRoute("setHeader.xml");
        assertFrom(route, "seda:a");
        SetHeaderDefinition node = assertNthProcessorInstanceOf(SetHeaderDefinition.class, route, 0);
        assertEquals("oldBodyValue", node.getName());
        assertExpression(node.getExpression(), "simple", "body");
        assertChildTo(route, "mock:b", 1);
    }

    @Test
    public void testParseSetHeaderToConstantXml() throws Exception {
        RouteDefinition route = assertOneRoute("setHeaderToConstant.xml");
        assertFrom(route, "seda:a");
        SetHeaderDefinition node = assertNthProcessorInstanceOf(SetHeaderDefinition.class, route, 0);
        assertEquals("theHeader", node.getName());
        assertExpression(node.getExpression(), "constant", "a value");
        assertChildTo(route, "mock:b", 1);
    }

    @Test
    public void testParseConvertBodyXml() throws Exception {
        RouteDefinition route = assertOneRoute("convertBody.xml");
        assertFrom(route, "seda:a");
        ConvertBodyDefinition node = assertOneProcessorInstanceOf(ConvertBodyDefinition.class, route);
        assertEquals("java.lang.Integer", node.getType());
    }

    @Test
    public void testParseConvertHeaderXml() throws Exception {
        RouteDefinition route = assertOneRoute("convertHeader.xml");
        assertFrom(route, "seda:a");
        ConvertHeaderDefinition node = assertOneProcessorInstanceOf(ConvertHeaderDefinition.class, route);
        assertEquals("foo", node.getName());
        assertEquals("java.lang.Integer", node.getType());
    }

    @Test
    public void testParseConvertVariableXml() throws Exception {
        RouteDefinition route = assertOneRoute("convertVariable.xml");
        assertFrom(route, "seda:a");
        ConvertVariableDefinition node = assertOneProcessorInstanceOf(ConvertVariableDefinition.class, route);
        assertEquals("foo", node.getName());
        assertEquals("java.lang.Integer", node.getType());
    }

    @Test
    public void testParseRoutingSlipXml() throws Exception {
        RouteDefinition route = assertOneRoute("routingSlip.xml");
        assertFrom(route, "seda:a");
        RoutingSlipDefinition<?> node = assertOneProcessorInstanceOf(RoutingSlipDefinition.class, route);
        assertEquals("destinations", node.getExpression().getExpression());
        assertEquals(RoutingSlipDefinition.DEFAULT_DELIMITER, node.getUriDelimiter());
    }

    @Test
    public void testParseRoutingSlipWithHeaderSetXml() throws Exception {
        RouteDefinition route = assertOneRoute("routingSlipHeaderSet.xml");
        assertFrom(route, "seda:a");
        RoutingSlipDefinition<?> node = assertOneProcessorInstanceOf(RoutingSlipDefinition.class, route);
        assertEquals("theRoutingSlipHeader", node.getExpression().getExpression());
        assertEquals(RoutingSlipDefinition.DEFAULT_DELIMITER, node.getUriDelimiter());
    }

    @Test
    public void testParseRoutingSlipWithHeaderAndDelimiterSetXml() throws Exception {
        RouteDefinition route = assertOneRoute("routingSlipHeaderAndDelimiterSet.xml");
        assertFrom(route, "seda:a");
        RoutingSlipDefinition<?> node = assertOneProcessorInstanceOf(RoutingSlipDefinition.class, route);
        assertEquals("theRoutingSlipHeader", node.getExpression().getExpression());
        assertEquals("#", node.getUriDelimiter());
    }

    @Test
    public void testParseRouteWithChoiceXml() throws Exception {
        RouteDefinition route = assertOneRoute("routeWithChoice.xml");
        assertFrom(route, "seda:a");

        ChoiceDefinition choice = assertOneProcessorInstanceOf(ChoiceDefinition.class, route);
        List<WhenDefinition> whens = assertListSize(choice.getWhenClauses(), 2);
        assertChildTo("when(0)", whens.get(0), "seda:b");
        assertChildTo("when(1)", whens.get(1), "seda:c");

        OtherwiseDefinition otherwise = choice.getOtherwise();
        assertNotNull(otherwise, "Otherwise is null");
        assertChildTo("otherwise", otherwise, "seda:d");
    }

    @Test
    public void testParseSplitterXml() throws Exception {
        RouteDefinition route = assertOneRoute("splitter.xml");
        assertFrom(route, "seda:a");

        SplitDefinition splitter = assertOneProcessorInstanceOf(SplitDefinition.class, route);
        assertExpression(splitter.getExpression(), "xpath", "/foo/bar");
        assertChildTo("to", splitter, "seda:b");
    }

    @Test
    public void testParseLoadBalance() throws Exception {
        RouteDefinition route = assertOneRoute("routeWithLoadBalance.xml");
        assertFrom(route, "seda:a");
        LoadBalanceDefinition loadBalance = assertOneProcessorInstanceOf(LoadBalanceDefinition.class, route);
        assertEquals(3, loadBalance.getOutputs().size(), "Here should have 3 output here");
        boolean b = loadBalance.getLoadBalancerType() instanceof RoundRobinLoadBalancerDefinition;
        assertTrue(b, "The loadBalancer should be RoundRobinLoadBalancerDefinition");
    }

    @Test
    public void testParseStickyLoadBalance() throws Exception {
        RouteDefinition route = assertOneRoute("routeWithStickyLoadBalance.xml");
        assertFrom(route, "seda:a");
        LoadBalanceDefinition loadBalance = assertOneProcessorInstanceOf(LoadBalanceDefinition.class, route);
        assertEquals(3, loadBalance.getOutputs().size(), "Here should have 3 output here");
        boolean b = loadBalance.getLoadBalancerType() instanceof StickyLoadBalancerDefinition;
        assertTrue(b, "The loadBalancer should be StickyLoadBalancerDefinition");
        StickyLoadBalancerDefinition strategy = (StickyLoadBalancerDefinition) loadBalance.getLoadBalancerType();
        assertNotNull(strategy.getCorrelationExpression(), "the expression should not be null ");
    }

    @Test
    public void testParseFailoverLoadBalance() throws Exception {
        RouteDefinition route = assertOneRoute("routeWithFailoverLoadBalance.xml");
        assertFrom(route, "seda:a");
        LoadBalanceDefinition loadBalance = assertOneProcessorInstanceOf(LoadBalanceDefinition.class, route);
        assertEquals(3, loadBalance.getOutputs().size(), "Here should have 3 output here");
        boolean b = loadBalance.getLoadBalancerType() instanceof FailoverLoadBalancerDefinition;
        assertTrue(b, "The loadBalancer should be FailoverLoadBalancerDefinition");
        FailoverLoadBalancerDefinition strategy = (FailoverLoadBalancerDefinition) loadBalance.getLoadBalancerType();
        assertEquals(2, strategy.getExceptions().size(), "there should be 2 exceptions");
    }

    @Test
    public void testParseRandomLoadBalance() throws Exception {
        RouteDefinition route = assertOneRoute("routeWithRandomLoadBalance.xml");
        assertFrom(route, "seda:a");
        LoadBalanceDefinition loadBalance = assertOneProcessorInstanceOf(LoadBalanceDefinition.class, route);
        assertEquals(3, loadBalance.getOutputs().size(), "Here should have 3 output here");
        boolean b = loadBalance.getLoadBalancerType() instanceof RandomLoadBalancerDefinition;
        assertTrue(b, "The loadBalancer should be RandomLoadBalancerDefinition");
    }

    @Test
    public void testParseTopicLoadBalance() throws Exception {
        RouteDefinition route = assertOneRoute("routeWithTopicLoadBalance.xml");
        assertFrom(route, "seda:a");
        LoadBalanceDefinition loadBalance = assertOneProcessorInstanceOf(LoadBalanceDefinition.class, route);
        assertEquals(3, loadBalance.getOutputs().size(), "Here should have 3 output here");
        boolean b = loadBalance.getLoadBalancerType() instanceof TopicLoadBalancerDefinition;
        assertTrue(b, "The loadBalancer should be TopicLoadBalancerDefinition");
    }

    @Test
    public void testParseHL7DataFormat() throws Exception {
        RouteDefinition route = assertOneRoute("routeWithHL7DataFormat.xml");
        assertFrom(route, "seda:a");
    }

    @Test
    public void testParseXMLSecurityDataFormat() throws Exception {
        RouteDefinition route = assertOneRoute("routeWithXMLSecurityDataFormat.xml");
        assertFrom(route, "seda:a");
    }

    @Test
    public void testParseTidyMarkupDataFormat() throws Exception {
        RouteDefinition route = assertOneRoute("routeWithTidyMarkupDataFormat.xml");
        assertFrom(route, "seda:a");
    }

    @Test
    public void testParseTidyMarkupDataFormatAndAllowNullBody() throws Exception {
        RouteDefinition route = assertOneRoute("routeWithTidyMarkupDataFormatAndAllowNullBody.xml");
        assertFrom(route, "seda:a");
        UnmarshalDefinition unmarshal = assertNthProcessorInstanceOf(UnmarshalDefinition.class, route, 0);
        assertEquals("true", unmarshal.getAllowNullBody(), "The unmarshaller should allow null body");
    }

    @Test
    public void testParseRSSDataFormat() throws Exception {
        RouteDefinition route = assertOneRoute("routeWithRSSDataFormat.xml");
        assertFrom(route, "seda:a");
    }

    @Test
    public void testParseJSonDataFormat() throws Exception {
        RouteDefinition route = assertOneRoute("routeWithJSonDataFormat.xml");
        assertFrom(route, "seda:a");
    }

    @Test
    public void testParseJaxbDataFormat() throws Exception {
        RouteDefinition route = assertOneRoute("routeWithJaxbDataFormat.xml");
        assertFrom(route, "seda:a");
    }

    @Test
    public void testParseFlatpackDataFormat() throws Exception {
        RouteDefinition route = assertOneRoute("routeWithFlatpackDataFormat.xml");
        assertFrom(route, "seda:a");
    }

    @Test
    public void testParseCvsDataFormat() throws Exception {
        RouteDefinition route = assertOneRoute("routeWithCvsDataFormat.xml");
        assertFrom(route, "seda:a");
    }

    @Test
    public void testParseZipFileDataFormat() throws Exception {
        RouteDefinition route = assertOneRoute("routeWithZipFileDataFormat.xml");
        assertFrom(route, "seda:a");
    }

    @Test
    public void testParseBindyDataFormat() throws Exception {
        RouteDefinition route = assertOneRoute("routeWithBindyDataFormat.xml");
        assertFrom(route, "seda:a");
    }

    @Test
    public void testParseBatchResequencerXml() throws Exception {
        RouteDefinition route = assertOneRoute("resequencerBatch.xml");
        ResequenceDefinition resequencer = assertOneProcessorInstanceOf(ResequenceDefinition.class, route);
        assertNull(resequencer.getStreamConfig());
        assertNotNull(resequencer.getBatchConfig());
        assertEquals(Integer.toString(500), resequencer.getBatchConfig().getBatchSize());
        assertEquals(Long.toString(2000L), resequencer.getBatchConfig().getBatchTimeout());
    }

    @Test
    public void testParseStreamResequencerXml() throws Exception {
        RouteDefinition route = assertOneRoute("resequencerStream.xml");
        ResequenceDefinition resequencer = assertOneProcessorInstanceOf(ResequenceDefinition.class, route);
        assertNotNull(resequencer.getStreamConfig());
        assertNull(resequencer.getBatchConfig());
        assertEquals(Integer.toString(1000), resequencer.getStreamConfig().getCapacity());
        assertEquals(Long.toString(2000L), resequencer.getStreamConfig().getTimeout());
    }

    @Test
    public void testLoop() throws Exception {
        RouteDefinition route = assertOneRoute("loop.xml");
        LoopDefinition loop = assertOneProcessorInstanceOf(LoopDefinition.class, route);
        assertNotNull(loop.getExpression());
        assertEquals("constant", loop.getExpression().getLanguage());
    }

    // Implementation methods
    // -------------------------------------------------------------------------

    protected RouteDefinition assertOneRoute(String uri) throws JAXBException {
        RouteContainer context = assertParseAsJaxb(uri);
        return assertOneElement(context.getRoutes());
    }

    protected void assertFrom(RouteDefinition route, String uri) {
        FromDefinition from = route.getInput();
        assertEquals(uri, from.getUri(), "From URI");
    }

    protected void assertChildTo(String message, OutputNode route, String uri) {
        ProcessorDefinition<?> processor = assertOneElement(route.getOutputs());
        ToDefinition value = assertIsInstanceOf(ToDefinition.class, processor);
        String text = message + "To URI";
        log.info("Testing: {} is equal to: {} for processor: {}", text, uri, processor);
        assertEquals(uri, value.getUri(), text);
    }

    protected void assertTo(String message, ProcessorDefinition<?> processor, String uri) {
        ToDefinition value = assertIsInstanceOf(ToDefinition.class, processor);
        String text = message + "To URI";
        log.info("Testing: {} is equal to: {} for processor: {}", text, uri, processor);
        assertEquals(uri, value.getUri(), text);
    }

    protected void assertChildTo(OutputNode route, String... uris) {
        List<ProcessorDefinition<?>> list = assertListSize(route.getOutputs(), uris.length);
        int idx = 0;
        for (String uri : uris) {
            assertTo("output[" + idx + "] ", list.get(idx++), uri);
        }
    }

    protected void assertChildTo(OutputNode route, String uri, int toIdx) {
        List<ProcessorDefinition<?>> list = route.getOutputs();
        assertTo("to and idx=" + toIdx, list.get(toIdx), uri);
    }

    protected <T> T assertOneProcessorInstanceOf(Class<T> type, OutputNode route) {
        ProcessorDefinition<?> processor = assertOneElement(route.getOutputs());
        return assertIsInstanceOf(type, processor);
    }

    protected <T> T assertNthProcessorInstanceOf(Class<T> type, OutputNode route, int index) {
        ProcessorDefinition<?> processor = route.getOutputs().get(index);
        return assertIsInstanceOf(type, processor);
    }

    protected void assertExpression(ExpressionDefinition expression, String language, String languageExpression) {
        assertNotNull(expression, "Expression should not be null!");
        assertEquals(language, expression.getLanguage(), "Expression language");
        assertEquals(languageExpression, expression.getExpression(), "Expression");
    }
}
