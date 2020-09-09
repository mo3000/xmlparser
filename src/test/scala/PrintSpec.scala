import org.scalatest.flatspec.AnyFlatSpec
import org.suiwenbo.xmlparser.{TagElem, Tags}

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

  it should "printable" in {
    assertResult(List(
      "<div>yoyo</div>",
      "<div id=\"3\" data-index=\"0\">nihao</div>",
      "<div>\n  <p>p1 content</p>\n  <p>p2 content</p>\n  <div>\n    <p>p11 content</p>\n  </div>\n</div>"
    ))(tests1.map(_.toString))
  }
}
