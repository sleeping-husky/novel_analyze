package sample

import text.novel.Novel
import text.sentence.SentenceId
import text.word.{Word, WordBase}
import tokenize._

class HTMLTranslate {

}

object HTMLTranslate{
  private[this] val idDelimiter = ","
  private[this] val sentenceValuesFunctionName = "outputSentenceValues"
  private[this] val wrapperCSSClass = "wrapper"
  private[this] val columnCSSClass = "column"
  private[this] val jsonSentencesId = "sentences_json"
  private[this] val sentencesOutputId = "sentences_output"


  def makeJSONInScriptTag(json: String, id: String): String =
    s"""<script type=\"application/json\" id=\"$id\">
       |$json
       |</script>""".stripMargin

  def makeBaseData(novel: Novel): String =
      s"""<p>文字数：${novel.length}</p>
      |<p>セリフ割合：${Math.floor(novel.speechPercent).toInt}%</p>
      |<p>1文の平均文字数：${novel.lengthAverageOneSentence}</p>
      |<p>1文中の平均読点数：${novel.japaneseCommaAverageOneSentence}</p>""".stripMargin

  def makePartOfSpeechData(novel: Novel): String = {

    val words = novel.words
    def wordPart(wordData: (WordBase, Seq[SentenceId])): String = {
      val ids = "'" + wordData._2.map(s => s.value).mkString(idDelimiter) + "'"
      "<span onclick=\"" + sentenceValuesFunctionName + "(" + ids + ")\">" + wordData._1.value + "</span>"
    }

    val toData: (Word => Boolean) => String = wordFilter =>
      words.sortedWords(wordFilter)
      .map(d => "<p>" + d._1 + "（" + d._2.size + "）" + ":" + d._2.map(wordPart).mkString(", ") + "</p>").mkString("\n")
    def section(value: String): String = "<section>" + value + "</section>"
    def header(value: String): String = "<h3>" + value + "</h3>"

    val data = Seq(
      header(Verb.japaneseName) + toData(w => w.partOfSpeech == Verb),
      header(Noun.japaneseName + "（" + SuruVerbConjugation.japaneseName + "）") +
        toData(w => w.partOfSpeech == Noun && w.partOfSpeechSub == SuruVerbConjugation) +
      header(Noun.japaneseName) +
        toData(w => w.partOfSpeech == Noun &&
        w.partOfSpeechSub != ProperNoun && w.partOfSpeechSub != Pronoun && w.partOfSpeechSub != SuruVerbConjugation),
      header(ProperNoun.japaneseName) + toData(w => w.partOfSpeechSub == ProperNoun),
      header(Pronoun.japaneseName) + toData(w => w.partOfSpeechSub == Pronoun),
      header(Adjective.japaneseName) + toData(w => w.partOfSpeech == Adjective),
      header(Adverb.japaneseName) + toData(w => w.partOfSpeech == Adverb),
      header(Connective.japaneseName) + toData(w => w.partOfSpeech == Connective),
      header(Alphabet.japaneseName) + toData(w => w.partOfSpeechSub == Alphabet),
      header(Other.japaneseName) + toData(w => w.partOfSpeech == Other),
      header(Filler.japaneseName) + toData(w => w.partOfSpeech == Filler),
      header(Unknown.japaneseName) + toData(w => w.partOfSpeech == Unknown)
    ).map(section).mkString
    data
  }

  def makeScript(jsonId: String, outputId: String): String =
    s"""<script>
      |
      | function p(text){
      |   var element = document.createElement( "p" ) ;
      |   return element.textContent = text;
      | }
      | function $sentenceValuesFunctionName(sids){
      |  var sidSet = new Set(sids.split("$idDelimiter"));
      |  var elem = document.getElementById("$jsonId");
      |  var json = JSON.parse(elem.textContent);
      |  var sentenceValues = json.sentences.filter(s => sidSet.has(s.id)).map(s => s.data);
      |
      |  var output = document.getElementById("$outputId");
      |  while (output.firstChild) {output.removeChild(output.firstChild)};
      |  sentenceValues.forEach(function(element, index, array) {
      |    var pTag = document.createElement( "p" ) ;
      |    pTag.textContent = element;
      |    output.appendChild(pTag);
      |  });
      |
      | }
      |</script>""".stripMargin

  def makeStyle: String =
    s"""
       |<style type="text/css">
       |.$columnCSSClass {
       |  height: 100vh;
       |  width: 100%;
       |  margin: 10px;
       |  overflow:scroll;
       |}
       |.$wrapperCSSClass {
       |  display: flex;
       |}
       |</style>
     """.stripMargin

  def sampleHTML(novel: Novel): String = {
    val cssStyle = makeStyle
    val baseData = makeBaseData(novel)
    val partOfSpeechData = makePartOfSpeechData(novel)
    val json = JSONTranslate.from(novel.sentences)
    val jsonData = makeJSONInScriptTag(json, jsonSentencesId)
    val script = makeScript(jsonSentencesId, sentencesOutputId)

    s"""
       |<!DOCTYPE html>
       |<html lang="ja">
       |<head>
       |<meta charset="UTF-8">
       |<title lang="ja">novel analyze</title>
       |$cssStyle
       |</head>
       |<body>
       |<h1>novel analyze</h1>
       |<div class="$wrapperCSSClass">
       |<div class="$columnCSSClass">
       |<article>
       |	<h2>基本データ</h2>
       | $baseData
       |</article>
       |
       |<article>
       |	<h2>品詞別データ</h2>
       |	$partOfSpeechData
       |</article>
       |</div>
       |
       |<div class="$columnCSSClass">
       |<h2>文章</h2>
       |<article id="$sentencesOutputId">
       |</article>
       |</div>
       |
       |</body>
       |$script
       |$jsonData
       |</html>
     """.stripMargin
  }
}
