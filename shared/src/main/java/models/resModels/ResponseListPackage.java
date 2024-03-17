package models.resModels;

import models.Game;
import java.util.Collection;

public record ResponseListPackage(Collection<Game> games) {
}
