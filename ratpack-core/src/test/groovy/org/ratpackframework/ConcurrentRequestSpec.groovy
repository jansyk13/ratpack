package org.ratpackframework

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class ConcurrentRequestSpec extends RatpackSpec {

  def "can serve requests concurrently without mixing up params"() {
    given:
    ratpackFile << """
      get("/:id") {
        text getRequest().urlParams.id + ":" + getRequest().queryParams.id[0]
      }
    """

    when:
    startApp()

    def threads = 10
    def latch = new CountDownLatch(threads)
    def results = []
    threads.times {
      results << null
    }

    threads.times { i ->
      Thread.start {
        try {
          def text = urlGetText("$i?id=$i")
          assert text ==~ "\\d+:\\d+"
          def (id, value) = text.split(':').collect { it.toInteger() }
          results[id] = value
        } finally {
          latch.countDown()
        }
      }
    }

    latch.await(30, TimeUnit.SECONDS)

    then:
    (0..<threads).each {
      assert results[it] == it
    }
  }


}
