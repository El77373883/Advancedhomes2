# AdvancedHomes Premium 🏠

Plugin premium de homes para **Spigot 1.21** con GUI, cooldown y múltiples comandos.

## Comandos

| Comando | Descripción | Permiso |
|--------|-------------|---------|
| `/ahome` | Abre el GUI de homes | `advancedhomes.use` |
| `/ahome <nombre>` | Te teletransporta a un home | `advancedhomes.use` |
| `/asethome <nombre>` | Establece un home en tu posición | `advancedhomes.use` |
| `/adeletehome <nombre>` | Elimina un home | `advancedhomes.use` |
| `/ahomelist` | Lista todos tus homes en el chat | `advancedhomes.use` |
| `/ahomes` | Abre el GUI de homes | `advancedhomes.use` |
| `/ahometp <jugador> <home>` | Ir al home de otro jugador | `advancedhomes.tp.others` |
| `/ahomereload` | Recarga la configuración | `advancedhomes.admin` |

## Permisos

| Permiso | Descripción | Default |
|---------|-------------|---------|
| `advancedhomes.use` | Comandos básicos | Todos |
| `advancedhomes.tp.others` | Ir al home de otros | OP |
| `advancedhomes.admin` | Recargar config | OP |
| `advancedhomes.cooldown.bypass` | Sin cooldown | OP |

## Compilar desde GitHub

1. Haz fork o sube estos archivos a tu repositorio
2. Ve a **Actions** en GitHub
3. El plugin se compila automáticamente al hacer push
4. Descarga el `.jar` desde **Artifacts**

## Configuración

Edita `config.yml` en el servidor:
- `settings.cooldown` → segundos de cooldown (default: 3)
- `settings.max-homes` → homes máximos (-1 = ilimitados)
- Todos los mensajes son editables con colores `&`
