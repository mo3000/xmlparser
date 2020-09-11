import org.scalatest.flatspec.AnyFlatSpec
import org.suiwenbo.xmlparser.{CDATA, Parser, TagElem, Tags}

class PrintSpec extends AnyFlatSpec {
  val tests1 = List(
    TagElem("div", Map.empty, "yoyo"),
    TagElem("div", Map("id" -> "3", "data-index" -> "0"), "nihao"),
    Tags("div", Map.empty, List(
      TagElem("p", Map.empty, "p1 content"),
      TagElem("p", Map.empty, "p2 content"),
      Tags("div", Map.empty, List(
        TagElem("p", Map.empty, "p11 content"),
      )),
    ))
  )

  it should "be printable" in {
    assertResult(List(
      "<div>yoyo</div>",
      "<div id=\"3\" data-index=\"0\">nihao</div>",
      "<div>\n  <p>p1 content</p>\n  <p>p2 content</p>\n  <div>\n    <p>p11 content</p>\n  </div>\n</div>"
    ))(tests1.map(_.pretty))
  }

  it should "parse basic xml" in {
    val parser = new Parser()
    parser.parseString("<div id=\"nihao\"><div id=\"2\"><p>p1p1p1</p><p>p2p2p2</p></div></div>")
  }

  it should "parse xml with CDATA" in {
    val parser = new Parser()
    val cdataStr = "yoyo qiekenao function yoyo() {qiekenao();} "
    parser.parseString(s"<div><![CDATA[$cdataStr]]></div>") match {
      case Tags(_, _, List(CDATA(content))) => content == cdataStr
      case _ => throw new RuntimeException("parse error")
    }

  }

  it should "parse a complex example" in {
    val parser = new Parser()
    val xml = parser.parseFile(getClass.getResource("/teacher.xml").getPath)
    xml match {
      case Tags("teacher", _, List(age, name, card, Tags("students", _, List(stu1, stu2, stu3)))) =>
    }
    assert(xml.pretty.nonEmpty)
  }
}
