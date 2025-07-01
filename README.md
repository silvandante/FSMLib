# FiniteStateMachine

A lightweight Kotlin library for modeling finite state machines using sealed classes and clean transition logic.

## 🚦 Overview

This library provides a simple, type-safe way to define and manage FSMs in Kotlin. It uses sealed classes to enforce state constraints at compile time, making it ideal for:

- UI state management  
- Navigation flows  
- Game mechanics  
- Workflow engines

## ✨ Features

- 🧩 Sealed class-based states and events
- ✅ Explicit state transitions via `onEvent()`
- 🔒 Type-safe and deterministic logic
- 🧪 Easy unit testing and debugging
- 📚 Minimal, idiomatic Kotlin design

## 📐 How It Works

Define your states by extending the generic `State` interface and implementing how each state responds to events.

```kotlin
sealed class MyState : State<MyState, MyEvent> {
    data object Idle : MyState() {
        override fun onEvent(event: MyEvent) = when (event) {
            MyEvent.Start -> Loading
            else -> this
        }
    }

    data object Loading : MyState() {
        override fun onEvent(event: MyEvent) = when (event) {
            MyEvent.Success -> Success
            MyEvent.Failure -> Error
            else -> this
        }
    }

    data object Success : MyState() {
        override fun onEvent(event: MyEvent) = this
    }

    data object Error : MyState() {
        override fun onEvent(event: MyEvent) = Idle
    }
}

sealed interface MyEvent {
    object Start : MyEvent
    object Success : MyEvent
    object Failure : MyEvent
}
```

## 🚀 Using the StateMachine
Create a state machine and interact with it using sendEvent:
```kotlin
val machine = StateMachine(initialState = MyState.Idle)
machine.sendEvent(MyEvent.Start)    // -> Loading
machine.sendEvent(MyEvent.Success) // -> Success
```

## 📝 License
MIT License — free for personal and commercial use.
View license
