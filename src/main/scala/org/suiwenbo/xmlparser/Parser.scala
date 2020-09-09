package org.suiwenbo.xmlparser

import org.suiwenbo.xmlparser.XmlEntity.XmlProperty

import scala.collection.mutable
import scala.io.Source

class Parser {

  val skips = Set(' ', '\t', '\n')

  var pos: Int = 0
  var text: Array[Char] = Array.emptyCharArray

  def parseString(s: String): XmlEntity = {
    text = s.toCharArray
    pos = 0
    val result = xml()
    result
  }

  private def xmlListOrString(): Either[List[XmlEntity], String] = {
    skip()
    if (currentChar == '<') {
      val list = mutable.ListBuffer()[XmlEntity]
      while (isBeginTag) {
        list.addOne(xml())
      }
      Left(list.toList)
    } else {
      //pure string
      val startAt = pos
      while (pos < text.length && currentChar != '<') pos += 1
      if (pos >= text.length || currentChar != '<') error("xml not closed")
      Right(text.slice(startAt, pos).mkString)
    }
  }

  private def isBeginTag: Boolean = {
    var p = pos
    p += 1
    while (text(p).isWhitespace) p += 1
    text(p) == '/'
  }

  private def xml(): XmlEntity = {
    skip()
    assert(currentChar == '<')
    val (tagname, props) = matchTagBegin()
    val result = xmlListOrString() match {
      case Left(v) => Tags(tagname, props, v)
      case Right(v) => TagElem(tagname, props, v)
    }
    matchTagClose(tagname)
    result
  }

  private def skip(): Unit = while (isSkipable) pos += 1

  private def currentChar: Char = text(pos)

  private def matchTagBegin(): (String, XmlProperty) = {
    pos += 1
    skip()
    if (pos >= text.length || currentChar == '>') error("invalid xml")
    val name = tagname()
    skip()
    if (currentChar != '>') {
      val props = matchProps()
      assert(currentChar == '>')
      (name, props)
    } else {
      (name, Map.empty)
    }
  }

  private def tagname(): String = {
    val start = pos
    while (pos <= text.length && (currentChar != ' ' && currentChar != '>'))
      pos += 1
    text.slice(start, pos).mkString
  }

  private def matchProps(): XmlProperty = {
    val map = mutable.Map()[String, String]
    while (currentChar != '>') {
      skip()
      val key = fetchName()
      skip()
      assert(currentChar == '=', "xml tag property lacks of '=', key name: " + key)
      pos += 1
      skip()
      assert(currentChar == '"', "xml tag property value lacks of '\"', key name: " + key)
      pos += 1
      val value = fetchName()
      if (pos >= text.length || currentChar != '"') error("invalid xml, props not closed, prop: " + key)
      map(key) = value
      pos += 1
      skip()
    }
    map.toMap
  }

  private def error(message: String) = throw new RuntimeException(message)

  private def matchTagClose(name: String): Unit = {
    skip()
    assert(currentChar == '/', "xml tag not closed, tagname: " + name)
    pos += 1
    skip()
    val closename = fetchName()
    assert(name == closename, s"xml tag doesn't match, tagname $closename, surposed to be $name")
    skip()
    assert(currentChar == '>', s"xml tag doesn't close tagname $name")
    pos += 1
  }

  private def fetchName(): String = {
    val startAt = pos
    while (pos <= text.length && currentChar.isLetterOrDigit || currentChar == '_') pos += 1
    text.slice(startAt, pos).mkString
  }

  private def isSkipable: Boolean = skips.contains(text(pos))

  def parseFile(s: String): XmlEntity = {
    val f = Source.fromFile(s)
    try {
      parseString(f.toString)
    } finally {
      f.close()
    }
  }
}
