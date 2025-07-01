package com.walker.finitestatemachine

// Define a sealed class for custom states (extend this)
open class State {
    // Add custom states here
}

// Define a sealed class for custom events (extend this)
open class Event {
    // Add custom events here
}

// Define a StateListener interface
interface StateListener {
    fun onStateChanged(newState: State)
}

// Define a StateMachine class
class StateMachine(initialState: State, transitions: Map<Pair<State, Event>, State>) {

    private var currentState: State = initialState

    // Store transitions in a map
    private val transitions: Map<Pair<State, Event>, State> = transitions

    // List to hold registered listeners
    private val listeners = mutableListOf<StateListener>()

    // Validate that each state has at least one transition
    init {
        if (transitions.isEmpty()) {
            throw IllegalArgumentException("Something is wrong with transitions, no transitions were recognized.")
        }
        val statesWithNoTransitions = transitions.keys.map { it.first }.toSet()
            .filter { state -> transitions.none { (key, _) -> key.first == state } }
        if (statesWithNoTransitions.isNotEmpty()) {
            throw IllegalArgumentException("The following states have no transitions defined: $statesWithNoTransitions")
        }
    }

    // Register a listener
    fun addListener(listener: StateListener) {
        listeners.add(listener)
    }

    // Unregister a listener
    fun removeListener(listener: StateListener) {
        listeners.remove(listener)
    }

    // Notify all listeners about the state change
    private fun notifyListeners(newState: State) {
        listeners.forEach { it.onStateChanged(newState) }
    }

    // Send an event to the state machine
    fun sendEvent(event: Event): State {
        val nextState = transitions[Pair(currentState, event)]
        return if (nextState != null) {
            currentState = nextState
            println("State changed to: $currentState")
            notifyListeners(currentState) // Notify listeners
            currentState
        } else {
            throw IllegalStateException("Invalid transition: Cannot transition from state '$currentState' with event '$event'")
        }
    }

    // Get the current state
    fun getCurrentState(): State {
        return currentState
    }
}

fun transitions(block: TransitionBuilder.() -> Unit): Map<Pair<State, Event>, State> {
    return TransitionBuilder().apply(block).build()
}

class TransitionBuilder {
    private val transitions = mutableMapOf<Pair<State, Event>, State>()

    // Enforce the use of this method to add transitions
    fun transition(pair: Pair<State, Event>, newState: State) {
        transitions[pair] = newState
    }

    fun build(): Map<Pair<State, Event>, State> = transitions
}

//// Define custom states
//sealed class AppState : State() {
//    object Initial : AppState()
//    object Loading : AppState()
//    object Success : AppState()
//    object Error : AppState()
//}
//
//// Define custom events
//sealed class AppEvent : Event() {
//    object StartLoading : AppEvent()
//    object DataLoaded : AppEvent()
//    object ErrorOccurred : AppEvent()
//}

// Create a class that implements the StateListener interface
class AppStateListener : StateListener {
    override fun onStateChanged(newState: State) {
        println("Listener: State changed to $newState")
    }
}
//
//fun main(): Unit = runBlocking {
//    val appTransitions = transitions {
//        transition(AppState.Initial to AppEvent.StartLoading, AppState.Loading)
//        transition(AppState.Loading to AppEvent.DataLoaded, AppState.Success)
//        transition(AppState.Loading to AppEvent.ErrorOccurred, AppState.Error)
//    }
//
//    // Create the state machine
//    val stateMachine = StateMachine(AppState.Initial, appTransitions)
//
//    // Register the listener
//    val listener = AppStateListener()
//    stateMachine.addListener(listener)
//
//    try {
//        stateMachine.sendEvent(AppEvent.StartLoading)
//        delay(2000)
//        stateMachine.sendEvent(AppEvent.DataLoaded)
//        delay(2000)
//        stateMachine.sendEvent(MainActivity.AppEvent.ErrorOccurred)
//    } catch (e: IllegalStateException) {
//        println("Error: ${e.message}")
//    }
//}