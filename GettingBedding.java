public interface GettingBedding {
    default String giveSomething(){
        return "The item is given.";
    }
    default String takeSomething(){
        return "The item is taken.";
    }
}