package scalad

import org.benf.cfr.reader.{Main => CFR}

def decompileScala(config: Config): Unit = {
  import scala.quoted._
  import runtime.impl.printers._
  import scala.tasty.inspector._
  var tastyStr: String = null
  val inspector = new Inspector {
    def inspect(using Quotes)(tastys: List[Tasty[quotes.type]]): Unit = {
      import quotes.reflect._
      for tasty <- tastys do
        tastyStr = SourceCode.showTree(tasty.ast)(
          if(config.highlight) SyntaxHighlight.ANSI else SyntaxHighlight.plain, 
          fullNames = config.fullNames)
    }
  }
  TastyInspector.inspectTastyFiles(List(config.file))(inspector)
  println(tastyStr)
}

@main def run(args: String*) =
  args.toList match {
    case args =>
      Config.read(args).foreach { config =>
        if(config.file.endsWith(".tasty"))
          decompileScala(config)
        else if(config.file.endsWith(".class"))
          CFR.main(args.toArray)
        else
          println(s"Invalid file extension ${config.file}")
      }
  }
  