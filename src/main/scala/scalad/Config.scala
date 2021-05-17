package scalad

import org.benf.cfr.reader.{Main => CFR}

case class Config(
  file: String, 
  highlight: Boolean = true,
  fullNames: Boolean = false,
  cfrHelp: Boolean = false)

object Config:

  enum Mode:
    case Scala, Java, Both    

  val help = """
ScALAD - Decompiler for Scala 3

scalad [options] file

Decompiles to Java if provided with a .class file,
and decompiles to Scala if provided with a .tasty file.

Options:
  --help       this help


Tasty file options:
  --highlight  (boolean)       default: true
  --fullNames  (boolean)       default: false

Class file options (from CFR decompiler):
    """

  def read(args: List[String]): Option[Config] = 
    args.lastOption match {
      case None =>
        println("File parameter missing. See --help")
        None
      case Some("--help") =>
        println(help)
        CFR.main(args.toArray)
        None
      case Some(file) =>
        def loop(args: List[String], config: Config): Option[Config] =
          args match {
            case Nil => Some(config)
            case arg :: tail =>
              val updated = 
                arg.split('=').toList match {
                  case "--highlight" :: Nil => Some(config.copy(highlight = true))
                  case "--highlight" :: "false" :: Nil => Some(config.copy(highlight = false))
                  case "--highlight" :: "true" :: Nil => Some(config.copy(highlight = true))
                  case "--fullNames" :: Nil => Some(config.copy(fullNames = true))
                  case "--fullNames" :: "false" :: Nil => Some(config.copy(fullNames = false))
                  case "--fullNames" :: "true" :: Nil => Some(config.copy(fullNames = true))
                  case _ =>
                    println(s"invalid option $arg. See --help")
                    None
                }
              updated.flatMap(loop(tail, _))
          }
        loop(args.dropRight(1), Config(file))
    }