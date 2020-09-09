package org.suiwenbo.xmlparser

import org.suiwenbo.xmlparser.XmlEntity.XmlProperty


object XmlEntity {
  type XmlProperty = Map[String, String]
}


sealed trait XmlEntity {
  var indent: Int = 0

  def formatProps(props: XmlProperty): String =
    (if (props.nonEmpty) " " else "") +
      props.map(kv => "%s=\"%s\"".format(kv._1, kv._2)).mkString(" ")

  def indentStr: String = "  " * indent

  def prettyPrint: String = {
    this match {
      case TagElem(name, props, content) =>
        s"$indentStr<$name${formatProps(props)}>$content</$name>"
      case Tags(name, props, children) =>
        s"$indentStr<$name${formatProps(props)}>\n" +
          children.map {
            _.addIndent(indent).toString
          }.mkString("\n") +
          s"\n$indentStr</$name>"
      case CDATA(content) =>
        s"$indentStr<![CDATA[$content]]>"
    }
  }

  def addIndent(incr: Int=0): XmlEntity = {
    indent += 1 + incr
    this
  }

}

case class TagElem(name: String, props: XmlProperty, content: String) extends XmlEntity
case class Tags(name: String, props: XmlProperty, children: List[XmlEntity]) extends XmlEntity
case class CDATA(content: String) extends XmlEntity
