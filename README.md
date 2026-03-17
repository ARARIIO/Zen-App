# Zen Timer

Приложение для фокуса в стиле Pomodoro с тёмной темой и неоновыми акцентами.

## Возможности

- **Таймер** — интервалы фокуса и коротких перерывов с точным отсчётом
- **Выбор задачи** — модальное окно с полем ввода и тегами
- **Путь (статистика)** — «созвездие фокуса» и история сессий
- **Настройки** — длительность фокуса и перерывов, звуки, виброотклик
- **Звуки и вибрация** — фоновые звуки и haptic feedback

## Технологии

| Компонент | Технология |
|-----------|------------|
| UI | Jetpack Compose, Material 3 |
| Навигация | Navigation 3 |
| База данных | Room 3 |
| Настройки | DataStore Preferences |
| DI | Koin |
| Язык | Kotlin |

## Требования

- Android 8.0 (API 26) и выше
- compileSdk 36
- Kotlin 2.3+
- JDK 21

## Сборка

```bash
./gradlew assembleDebug
```

Установка на устройство:

```bash
./gradlew installDebug
```

## Структура проекта

```
app/src/main/kotlin/zentimer/app/
├── data/           # Слой данных
│   ├── audio/      # Воспроизведение звуков
│   ├── local/      # Room: БД, DAO, сущности
│   ├── preferences # DataStore
│   └── repository # Репозитории
├── di/             # Koin-модули
├── domain/         # Бизнес-логика (TimerManager)
├── presentation/   # UI и ViewModel
│   ├── navigation/
│   ├── path/       # Экран «Путь»
│   ├── settings/   # Экран настроек
│   └── timer/      # Экран таймера
└── ui/theme/       # Цвета, шрифты, GlowModifier
```

## Лицензия

MIT
