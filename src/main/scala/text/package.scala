package object text {
  val FULL_JAPANESE_PERIOD = "。"
  val FULL_JAPANESE_COMMA = "、"
  val FULL_SQUARE_BRACKET_OPENED = "「"
  val FULL_SQUARE_BRACKET_CLOSED = "」"

  sealed abstract class NewLineChars(val value: String)
  case object CR extends NewLineChars("\r")
  case object LF extends NewLineChars("\n")
  case object CRLF extends NewLineChars("\r\n")

  object NewLineChars{
    def nlToBR(value: String): String = value.replaceAll(CRLF.value, "<br>")
      .replaceAll(CR.value, "<br>").replaceAll(LF.value, "<br>")
  }

}
