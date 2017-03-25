package exercices

import support.HandsOnSuite

class end extends HandsOnSuite {
  exercice("Terminé") {
    DeleteMeToContinue(
      """___________    .__  .__       .__  __          __  .__                        ._.
        |\_   _____/___ |  | |__| ____ |__|/  |______ _/  |_|__| ____   ____   ______  | |
        | |    __)/ __ \|  | |  |/ ___\|  \   __\__  \\   __\  |/  _ \ /    \ /  ___/  | |
        | |     \\  ___/|  |_|  \  \___|  ||  |  / __ \|  | |  (  <_> )   |  \\___ \    \|
        | \___  / \___  >____/__|\___  >__||__| (____  /__| |__|\____/|___|  /____  >   __
        |     \/      \/             \/              \/                    \/     \/    \/
        |
        |Tu as terminé ce cours, tu es maintenant prêt à te lancer sur des projets Scala ;)
      """.stripMargin
    )
  }
}
