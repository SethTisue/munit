package munit

class FrameworkSuite extends BaseFrameworkSuite {
  val tests: List[FrameworkTest] = List[FrameworkTest](
    // InterceptFrameworkSuite,
    CiOnlyFrameworkSuite,
    DiffProductFrameworkSuite,
    FailFrameworkSuite,
    TestNameFrameworkSuite,
    ScalaVersionFrameworkSuite,
    FixtureFrameworkSuite,
    TagsIncludeFramweworkSuite,
    TagsIncludeExcludeFramweworkSuite,
    TagsExcludeFramweworkSuite
  )
  tests.foreach { t =>
    check(t)
  }
}
