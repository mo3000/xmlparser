package org.suiwenbo.xmlparser

import org.suiwenbo.xmlparser.Tag.{XmlEntity, XmlProperty}


object Tag {
  type XmlProperty = Map[String, String]
  type XmlEntity = Either[TagElem, Tags]
}


sealed trait Tag {

}

case class TagElem(name: String, props: List[XmlProperty], content: String) extends Tag
case class Tags(name: String, props: List[XmlProperty], children: List[XmlEntity])
