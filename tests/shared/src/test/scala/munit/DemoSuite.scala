package munit

abstract class DemoSuite extends FunSuite {
  def someCondition(n: Int): Boolean = n != 2
  test("source-locations") {
    assert(someCondition(1))
    assert(someCondition(2))
    assert(someCondition(3))
  }
  test("diffs") {
    case class User(name: String, age: Int)
    val john = User("John", age = 41)
    val susan = User("Susan", age = 42)
    assertEquals(john, susan)
  }

  override def munitFlakyOK: Boolean = true
  test("stack-traces".flaky) {
    List(List(1, 2, 3).iterator).iterator.flatten.foreach { i =>
      require(i < 2, i)
    }
  }
}
