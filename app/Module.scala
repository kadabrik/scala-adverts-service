import com.google.inject.AbstractModule
import repositories.{CarRepository, InMemoryCarRepository}

class Module extends AbstractModule {

  override def configure() = {
    bind(classOf[CarRepository]).to(classOf[InMemoryCarRepository])
  }

}
