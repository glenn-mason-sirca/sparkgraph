package sparkgremlin.blueprints.io.build

/**
 * Created by kellrott on 2/8/14.
 */
class VertexRemoveBuild(val vertexId:AnyRef) extends BuildElement {
   def getVertexId : AnyRef = vertexId;
   def isEdge : Boolean = false;
   def isProperty : Boolean = false;
   def isRemoval = true;
   def getKey : String  = null;
   def getValue : Any = null;
   def getEdgeId : AnyRef = null;
   def getVertexInId : AnyRef = null;
   def getLabel : String = null;
 }