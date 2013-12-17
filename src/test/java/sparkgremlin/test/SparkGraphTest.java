package sparkgremlin.test;

import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.GraphTest;
import com.tinkerpop.blueprints.util.io.gml.GMLReaderTestSuite;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReaderTestSuite;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONReaderTestSuite;
import org.apache.spark.SparkContext;
import sparkgremlin.blueprints.SparkGraph;


import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

public class SparkGraphTest extends GraphTest {

    private SparkContext sc = null;
    public void setUp() {
        System.err.println("Setting Up");
        if (sc == null) {
            sc = new SparkContext("local", "SparkGraphTest", null, null, null, null);
        }
    }

    public void tearDown() {
        if (sc != null) {
            sc.stop();
            sc = null;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) { }
        }
    }

    public void testVertexTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new VertexTestSuite(this));
        printTestPerformance("VertexTestSuite", this.stopWatch());
    }

    public void testEdgeTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new EdgeTestSuite(this));
        printTestPerformance("EdgeTestSuite", this.stopWatch());
    }

    public void testGraphTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new GraphTestSuite(this));
        printTestPerformance("GraphTestSuite", this.stopWatch());
    }

    public void testGraphMLReaderTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new GraphMLReaderTestSuite(this));
        printTestPerformance("GraphMLReaderTestSuite", this.stopWatch());
    }

    public void testGMLReaderTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new GMLReaderTestSuite(this));
        printTestPerformance("GMLReaderTestSuite", this.stopWatch());
    }

    public void testGraphSONReaderTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new GraphSONReaderTestSuite(this));
        printTestPerformance("GraphSONReaderTestSuite", this.stopWatch());
    }


    public void testGraphQueryTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new GraphQueryTestSuite(this));
        printTestPerformance("GraphQueryTestSuite", this.stopWatch());
    }


    public void testVertexQueryTestSuite() throws Exception {
        this.stopWatch();
        doTestSuite(new VertexQueryTestSuite(this));
        printTestPerformance("VertexQueryTestSuite", this.stopWatch());
    }

    public void testVertexEdgeLabels2() {
        Graph graph = generateGraph();
        Vertex a = graph.addVertex(null);
        Vertex b = graph.addVertex(null);
        Vertex c = graph.addVertex(null);
        Edge aFriendB = graph.addEdge(null, a, b, convertLabel("friend"));
        Edge aFriendC = graph.addEdge(null, a, c, convertLabel("friend"));
        Edge aHateC = graph.addEdge(null, a, c, convertLabel("hate"));
        Edge cHateA = graph.addEdge(null, c, a, convertLabel("hate"));
        Edge cHateB = graph.addEdge(null, c, b, convertLabel("hate"));


        List<Edge> results = asList(a.getEdges(Direction.OUT, convertLabel("friend"), convertLabel("hate")));
        assertEquals(results.size(), 3);
        assertTrue(results.contains(aFriendB));
        assertTrue(results.contains(aFriendC));
        assertTrue(results.contains(aHateC));

        results = asList(a.getEdges(Direction.IN, convertLabel("friend"), convertLabel("hate")));
        assertEquals(results.size(), 1);
        assertTrue(results.contains(cHateA));

        results = asList(b.getEdges(Direction.IN, convertLabel("friend"), convertLabel("hate")));
        assertEquals(results.size(), 2);
        assertTrue(results.contains(aFriendB));
        assertTrue(results.contains(cHateB));

        results = asList(b.getEdges(Direction.IN, convertLabel("blah"), convertLabel("blah2"), convertLabel("blah3")));
        assertEquals(results.size(), 0);

        graph.shutdown();

    }

    @Override
    public Graph generateGraph() {
        return SparkGraph.generate(sc);
    }

    @Override
    public Graph generateGraph(String graphDirectoryName) {
        System.out.println(graphDirectoryName);
        return null;
    }

    public void doTestSuite(final TestSuite testSuite) throws Exception {
        String doTest = System.getProperty("testSparkGraph");
        if (doTest == null || doTest.equals("true")) {
            for (Method method : testSuite.getClass().getDeclaredMethods()) {
                if (method.getName().startsWith("test")) {
                    System.out.println("Testing " + method.getName() + "...");
                    method.invoke(testSuite);
                }
            }
        }
    }
}