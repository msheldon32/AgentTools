package AgentTools.Algorithms;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QLearningTest {
    @org.junit.jupiter.api.BeforeEach
    void setUp() {

    }

    @Test
    void getAction() {
        // getting an action with a greedy policy returns the correct action each time

        // getting an action with an epsilon-greedy policy returns the correct action (1-alpha) + (alpha*(1/n_states))
    }

    @Test
    void reinforce() {
        // reinforcing improves the total reward

        // reinforcing correctly improves the Q function
    }

    @Test
    void printQ() {
    }
}