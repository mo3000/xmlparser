This is a toy xmlparser written in scala.
usage:

```scala
val parser = new Parser()
val res = parser.parseString("<entity><name>heihei</name><age></age></entity>")
//print case class ast
println(res)
//or
res.prettyPrint
```
