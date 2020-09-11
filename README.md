A toy xmlparser written in scala.
====

- support tags recurtion
- tag may contain one or more property
- CDATA content

a posiable xml content like:

```xml
<teacher>
    <age>18</age>
    <name>AAAA</name>
    <card area="china" color="orange">123422343234</card>
    <students>
        <student><name>1</name></student>
        <student><name>2</name></student>
        <student>
            <name>3</name>
            <function>
            <![CDATA[
                <T>hello(T content) {console.log(content);}
            ]]>
            </function>
        </student>
    </students>
</teacher>
```

### usage:

```scala
val parser = new Parser()
val res = parser.parseString("<entity><name id=\"321\">heihei</name><age></age></entity>")
//print case class ast
println(res)
//or
println(res.pretty)
```
