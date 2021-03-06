---
id: filtering
title: Filtering tests
---

MUnit provides several options for selecting what tests to run.

## Run only a single test case

Use `testOnly -- $GLOB` to filter a fully qualified test name from the command
line.

```sh
# sbt shell
> testOnly -- *issue-456
> testOnly -- com.foo.controllers.*
```

Use `testOnly -- --only=$TEST_FILTER` to filter an individual test name from the
command line.

```scala mdoc:invisible
object Tests extends munit.FunSuite
import Tests._
```

```sh
# sbt shell
> testOnly -- --only=issue-456
```

Use `.only` to run only a single test without custom command-line arguments.

```scala mdoc
test("issue-457") {
  // will not run
}
test("issue-456".only) {
  // only test that runs
}
test("issue-455") {
  // will not run
}
```

## Ignore single test case

Use `.ignore` to skip an individual test case in a test suite.

```scala mdoc
test("issue-456".ignore) {
  // will not run
}
```

## Ignore single test case based on a dynamic conditions

Use `assume(condition, explanation)` to skip tests when some conditions do not
hold. For example, use `assume` to conditionally run tests based on the
operating system or the Scala compiler version.

```scala mdoc
import scala.util.Properties
test("paths") {
  assume(Properties.isLinux, "this test runs only on Linux")
  assume(Properties.versionNumberString.startsWith("2.13"), "this test runs only on Scala 2.13")
}
```

## Ignore entire test suite

Use the `@IgnoreSuite` annotation to skip all tests in a test suite.

```scala mdoc
@munit.IgnoreSuite
class MySuite extends munit.FunSuite {
  test("hello1") {
    // will not run
  }
  test("hello2") {
    // will not run
  }
  // ...
}
```

The `IgnoreSuite` annotation is only supported on the JVM. A workaround for
Scala.js and Scala Native is to mark the test suite as abstract so that it
doesn't run.

```diff
- @munit.IgnoreSuite
- class MySuite extends munit.FunSuite {
+ abstract class MySuite extends munit.FunSuite {
    test("hello1") {
      // will not run
```

## Ignore entire test suite based on a dynamic condition

Override `munitIgnore: Boolean` to skip an entire test suite based on a dynamic
condition.

```scala mdoc
class MyWindowsOnlySuite extends munit.FunSuite {
  override def munitIgnore: Boolean = !scala.util.Properties.isWin
  test("windows-only") {
    // only runs on Windows
  }
}
```

## Include and exclude tests based on tags

Use `--include-tags=$TAG1,$TAG2` and `--exclude-tags=$TAG1,$TAG2` to include and
exclude which tests to run based on tags. For example, imagine you have the
following test suite:

```scala mdoc
class TagsSuite extends munit.FunSuite {
  val include = new munit.Tag("include")
  val exclude = new munit.Tag("exclude")
  test("a".tag(include)) {}
  test("b".tag(exclude)) {}
  test("c".tag(include).tag(exclude)) {}
  test("d") {}
}
```

| Arguments                                       | Matching tests |
| :---------------------------------------------- | :------------: |
| `<no arguments>`                                | `<all tests>`  |
| `--include-tags=include`                        |     `a, c`     |
| `--include-tags=include --exclude-tags=exclude` |      `a`       |
| `--exclude-tags=exclude`                        |     `a, d`     |

## Group test suites with categories

> This feature is only supported on the JVM.

Use the `@Category(...)` annotation from JUnit to group tests suites together.

```scala mdoc
// package myapp
import org.junit.experimental.categories.Category

class Slow extends munit.Tag("Slow")
class Fast extends munit.Tag("Fast")

@Category(Array(classOf[Slow]))
class MySlowSuite extends munit.FunSuite {
  test("slow") {
    Thread.sleep(1000)
  }
  // ...
}
@Category(Array(classOf[Slow], classOf[Fast]))
class MySlowFastSuite extends munit.FunSuite {
  // ...
}
@Category(Array(classOf[Fast]))
class MyFastSuite extends munit.FunSuite {
  // ...
}
```

Next, use `--include-category=$CATEGORY` and `--exclude-category=$CATEGORY` to
determine what test suites to run from the command line.

```sh
# matches: MySlowSuite, MySlowFastSuite
> testOnly -- --include-category=myapp.Slow

# matches: MySlowSuite
> testOnly -- --include-category=myapp.Slow --exclude-category=myapp.Fast
```

## Filter tests cases based on a dynamic conditions

Override `munitTests()` to customize what tests get executed. For example, use
this feature to skip tests based on a dynamic condition.

```scala mdoc
import scala.util.Properties
case object Windows extends munit.Tag("Windows")
class MyWindowsTagSuite extends munit.FunSuite {
  override def munitTests(): Seq[Test] = {
    val default = super.munitTests()
    if (!Properties.isWin) default
    else default.filter(_.tags.contains(Windows))
  }
  test("files".tag(Windows)) {
    // will always run, including on Windows
  }
  test("files") {
    // will not run in Windows
  }
}
```
