package tokenize

import java.io.StringReader
import java.nio.file._
import java.util.Collections

import org.apache.lucene.analysis.ja.dict.UserDictionary
import org.apache.lucene.analysis.{Analyzer, StopFilter, TokenStream}
import org.apache.lucene.analysis.ja.{JapaneseAnalyzer, JapaneseBaseFormFilter, JapaneseTokenizer}
import org.apache.lucene.analysis.ja.tokenattributes.{BaseFormAttribute, PartOfSpeechAttribute}
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.util.Version

import scala.annotation.tailrec

object Tokenizer{
  private val defaultJapaneseAnalyzer = new JapaneseAnalyzer(null,
    JapaneseTokenizer.Mode.NORMAL,
    JapaneseAnalyzer.getDefaultStopSet,
    Collections.emptySet())

  private def withTokenStream(analyzer: JapaneseAnalyzer, text: String)(to: TokenStream => Tokens) = {
    val tokenStream = analyzer.tokenStream("",  text)
    try {
      tokenStream.reset()
      to(tokenStream)
    }
    finally tokenStream.close()
  }

  def of: String => Tokens = text => new Tokenizer(Tokenizer.defaultJapaneseAnalyzer).toTokens(text)
  def of(analyzer: JapaneseAnalyzer = Tokenizer.defaultJapaneseAnalyzer): String => Tokens =
    text => new Tokenizer(analyzer).toTokens(text)

}

class Tokenizer(analyzer: JapaneseAnalyzer = Tokenizer.defaultJapaneseAnalyzer) {

  private def toTokens(text: String): Tokens = {
    Tokenizer.withTokenStream(analyzer, text){tokenStream =>
      def token() = {
        val partOfSpeeches = tokenStream.getAttribute(classOf[PartOfSpeechAttribute]).getPartOfSpeech.split("-")
        Token(
          TokenBaseForm(Option(tokenStream.getAttribute(classOf[BaseFormAttribute]).getBaseForm)
            .getOrElse(tokenStream.getAttribute(classOf[CharTermAttribute]).toString)),
          PartOfSpeech.of(partOfSpeeches(0)),
          PartOfSpeechSub.of(if (partOfSpeeches.size > 1) partOfSpeeches(1) else NotExists.japaneseName)
        )
      }

      @tailrec
      def next(result: Seq[Token]): Seq[Token] = if (!tokenStream.incrementToken()) result.reverse else next(token +: result)
      Tokens(next(Seq()))
    }
  }
}


