/*
 * Adapted from https://github.com/scala-js/scala-js, see NOTICE.md.
 */

package com.geirsson.junit

import scala.util.Try

final class RunSettings(
    val color: Boolean,
    decodeScalaNames: Boolean,
    val verbose: Boolean,
    val logAssert: Boolean,
    val notLogExceptionClass: Boolean,
    val useSbtLoggers: Boolean,
    val tags: TagsFilter
) {
  def decodeName(name: String): String = {
    if (decodeScalaNames)
      Try(scala.reflect.NameTransformer.decode(name)).getOrElse(name)
    else name
  }
}
