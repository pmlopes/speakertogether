package io.ticofab.speakertogehter

import io.ticofab.speakertogether.{CsvUtil, SpeakerCsvEntry, SpeakerEnriched, StringUtils}
import org.scalatest.wordspec.AnyWordSpec

class AllTests extends AnyWordSpec {

  val resName = "allspeakersallevents.csv"
  val speakersFromCsv: List[SpeakerCsvEntry] = CsvUtil.parseSpeakerCsv(resName)

  "Speaker entries from the CSV file" must {

    "be parsed correctly" in {
      assert(speakersFromCsv.size == 7)
    }
  }

  "Enriched speakers" must {

    val speakersEnriched = speakersFromCsv.map(SpeakerEnriched.fromSpeakerCsvEntry)

    "have a normalized version" in {
      assertResult("adam sandor")(speakersEnriched.last.normalizedName)
    }

    // note: this test depends on an hardcoded table in a prod file
    "Have an optional twitter url" in {
      assertResult("https://twitter.com/adamsand0r")(speakersEnriched.last.maybeTwUrl.get)
    }
  }

  "A string with accented chars" should {
    "be normalized properly" in {
      val accents = "È,É,Ê,Ë,Û,Ù,Ï,Î,À,Â,Ô,è,é,ê,ë,û,ù,ï,î,à,â,ô,Ç,ç,Ã,ã,Õ,õ"
      val expected = "E,E,E,E,U,U,I,I,A,A,O,e,e,e,e,u,u,i,i,a,a,o,C,c,A,a,O,o"

      val accents2 = "çÇáéíóúýÁÉÍÓÚÝàèìòùÀÈÌÒÙãõñäëïöüÿÄËÏÖÜÃÕÑâêîôûÂÊÎÔÛ"
      val expected2 = "cCaeiouyAEIOUYaeiouAEIOUaonaeiouyAEIOUAONaeiouAEIOU"

      val accents3 =
        "Gisele Bündchen da Conceição e Silva foi batizada assim em homenagem à sua conterrânea de Horizontina, RS."
      val expected3 =
        "Gisele Bundchen da Conceicao e Silva foi batizada assim em homenagem a sua conterranea de Horizontina, RS."

      val accents4 = "/Users/rponte/arquivos-portalfcm/Eletron/Atualização_Diária-1.23.40.exe"
      val expected4 = "/Users/rponte/arquivos-portalfcm/Eletron/Atualizacao_Diaria-1.23.40.exe"

      assertResult(expected)(StringUtils.normalize(accents))
      assertResult(expected2)(StringUtils.normalize(accents2))
      assertResult(expected3)(StringUtils.normalize(accents3))
      assertResult(expected4)(StringUtils.normalize(accents4))
      assert(StringUtils.normalize("Ellen Körbes") == "Ellen Korbes")
      assert(StringUtils.normalize("Ádám Sándor") == "Adam Sandor")

    }
  }
}
