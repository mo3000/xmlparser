A toy xmlparser written in scala.
====

### usage:

```scala
val parser = new Parser()
val res = parser.parseString("<entity><name id=\"321\">heihei</name><age></age></entity>")
//print case class ast
println(res)
//or
println(res.pretty)
```
