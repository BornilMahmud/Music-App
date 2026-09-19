package com.example

import com.example.core.classification.ClassificationEngine
import com.example.core.model.AudioCategory
import com.example.core.playback.SmartShuffle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun testClassificationEngine() {
    val music = ClassificationEngine.classify(
      filePath = "/storage/emulated/0/Music/Coldplay/Yellow.mp3",
      fileName = "Yellow.mp3",
      title = "Yellow",
      artist = "Coldplay",
      durationMs = 260000L
    )
    assertEquals(AudioCategory.MUSIC, music.category)

    val recording = ClassificationEngine.classify(
      filePath = "/storage/emulated/0/Recordings/Meeting.m4a",
      fileName = "Meeting.m4a",
      title = "Meeting",
      durationMs = 120000L
    )
    assertEquals(AudioCategory.VOICE_RECORDING, recording.category)

    val sfx = ClassificationEngine.classify(
      filePath = "/storage/emulated/0/Download/click.wav",
      fileName = "click.wav",
      title = "Click Sound",
      durationMs = 800L
    )
    assertEquals(AudioCategory.SOUND_EFFECT, sfx.category)

    val whatsappVoiceNote = ClassificationEngine.classify(
      filePath = "/storage/emulated/0/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Voice Notes/202638/PTT-20260918-WA0014.opus",
      fileName = "PTT-20260918-WA0014.opus",
      title = "PTT-20260918-WA0014",
      durationMs = 45000L
    )
    assertEquals(AudioCategory.WHATSAPP_AUDIO, whatsappVoiceNote.category)

    val whatsappAudio = ClassificationEngine.classify(
      filePath = "/storage/emulated/0/WhatsApp/Media/WhatsApp Audio/AUD-20260915-WA0088.mp3",
      fileName = "AUD-20260915-WA0088.mp3",
      title = "AUD-20260915-WA0088",
      durationMs = 180000L
    )
    assertEquals(AudioCategory.WHATSAPP_AUDIO, whatsappAudio.category)
  }

  @Test
  fun testSmartShuffle() {
    val shuffle = SmartShuffle<String>()
    val list = listOf("Song A", "Song B", "Song C", "Song D")
    shuffle.setList(list, "Song A")

    val picked = mutableListOf<String>()
    repeat(4) {
      val next = shuffle.next()
      assertNotNull(next)
      picked.add(next!!)
    }
    assertEquals(4, picked.size)
  }
}
