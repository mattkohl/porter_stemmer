import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.io.Source


@RunWith(classOf[JUnitRunner])
class TestStemmer extends FunSuite {

  test("stemmer.b should be 'banning'") {
    val stemmer = new Stemmer()
    stemmer.add("banning")
    assert(stemmer.b === "banning")
  }

  test("Add a new word") {
    val stemmer = new Stemmer()
    stemmer.add("test")
    stemmer.add("banning")
    assert(stemmer.b === "banning")
  }

  test("Add a character") {
    val stemmer = new Stemmer()
    stemmer.add("bannin")
    stemmer.add('g')
    assert(stemmer.b === "banning")
  }

  test("The first character should be a consonant") {
    val stemmer = new Stemmer()
    stemmer.add("banning")
    assert(stemmer.isConsonant(0) === true)
  }

  test("The second character should not be a consonant") {
    val stemmer = new Stemmer()
    stemmer.add("banning")
    assert(stemmer.isConsonant(1) === false)
  }

  test("The second character should be a vowel") {
    val stemmer = new Stemmer()
    stemmer.add("banning")
    assert(stemmer.isVowel(1) === true)
  }

  test("There is a vowel in the stem 'ban' of the word 'banning'") {
    val stemmer = new Stemmer()
    stemmer.add("banning")
    assert(stemmer.isVowelInStem("ing") === true)
  }

  test("The number of consonant sequences in 'banning' is 2") {
    val stemmer = new Stemmer()
    stemmer.add("banning")
    assert(stemmer.getNumConsSeqs("banning") === 2)
  }

  test("There is a double consonant in 'nn'") {
    val stemmer = new Stemmer()
    stemmer.add("nn")
    assert(stemmer.isDoubleConsonant === true)
  }

  test("The output following step1 for 'banning' should be 'ban'") {
    val stemmer = new Stemmer()
    stemmer.add("banning")
    stemmer.step1()
    assert(stemmer.b === "ban")
  }

  test("The final output for 'banning' should be 'ban'") {
    val stemmer = new Stemmer()
    stemmer.add("banning")
    stemmer.step1()
    stemmer.step2()
    stemmer.step3()
    stemmer.step4()
    stemmer.step5a()
    stemmer.step5b()
    assert(stemmer.b === "ban")
  }

  test("Vocabulary test") {
    val vocabulary = Source.fromURL(getClass.getResource("/vocabulary.txt"))
    val stemmed = Source.fromURL(getClass.getResource("/stemmed.txt"))
    val stemmer = new Stemmer()

    val pairs = vocabulary.getLines zip stemmed.getLines

    for (pair <- pairs) {
      val trimmed = pair._1.trim()
      stemmer.add(trimmed)

      if (stemmer.b.length > 2) {
        stemmer.step1()
        stemmer.step2()
        stemmer.step3()
        stemmer.step4()
        stemmer.step5a()
        stemmer.step5b()
      }
      assert(stemmer.b === pair._2)
    }
  }

}
