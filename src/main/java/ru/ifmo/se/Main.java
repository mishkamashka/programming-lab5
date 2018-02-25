package ru.ifmo.se;

import ru.ifmo.se.enums.Material;
import ru.ifmo.se.enums.Patch;
import ru.ifmo.se.enums.Season;
import ru.ifmo.se.enums.State;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        Runtime.getRuntime().addShutdownHook(new Thread(app::save));
        app.start();
    }

    static void startGivenConv() {
        Known kozlik = new Known("Kozlik");
        Known neznaika = new Known("Neznaika");
        Unknown stranger = new Unknown();

        kozlik.addGeneralClothes(new Jacket("blue", Season.SUMMER), kozlik.getGeneralClothes());
        kozlik.addGeneralClothes(new Shirt("yellow"), kozlik.getGeneralClothes());
        kozlik.addGeneralClothes(new Jeans("black"), kozlik.getGeneralClothes());
        kozlik.addShoes(new Trainers("green"), kozlik.getShoes());

        neznaika.addGeneralClothes(new Shirt("pink"), neznaika.getGeneralClothes());
        neznaika.addGeneralClothes(new Trousers("blue"), neznaika.getGeneralClothes());
        neznaika.addAccessories(new Hat("light blue"), neznaika.getAccessories());
        neznaika.addShoes(new Boots("broun", Material.LEATHER), neznaika.getShoes());

        Patch[] trousersPatches = {Patch.BLACK_PATCH, Patch.BLACK_PATCH};
        stranger.addGeneralClothes(new Shirt("once blue, but now white", Material.FABRIC), stranger.getGeneralClothes());
        stranger.addGeneralClothes(new Trousers("dirty grey", Season.SUMMER, trousersPatches), stranger.getGeneralClothes());
        stranger.addAccessories(new Hat("yellow", Material.STRAW), stranger.getAccessories());

        Conversation conv = new Conversation();
        conv.addPerson(kozlik);
        conv.addPerson(neznaika);
        conv.tellStory(kozlik);
        conv.addPerson(stranger);


    }

    static void startLongConv() {
        Known kozlik = new Known("Kozlik");
        Known neznaika = new Known("Neznaika");
        Unknown stranger = new Unknown();

        kozlik.addGeneralClothes(new Jacket("blue", Season.SUMMER), kozlik.getGeneralClothes());
        kozlik.addGeneralClothes(new Shirt("yellow"), kozlik.getGeneralClothes());
        kozlik.addGeneralClothes(new Jeans("black with stripes"), kozlik.getGeneralClothes());
        kozlik.addShoes(new Trainers("green"), kozlik.getShoes());

        neznaika.addGeneralClothes(new Shirt("purple"), neznaika.getGeneralClothes());
        neznaika.addGeneralClothes(new Trousers("blue"), neznaika.getGeneralClothes());
        neznaika.addAccessories(new Hat("pink"), neznaika.getAccessories());
        neznaika.addShoes(new Boots("broun", Material.LEATHER), neznaika.getShoes());

        Patch[] trousersPatches = {Patch.WHITE_PATCH, Patch.BLACK_PATCH};
        stranger.addGeneralClothes(new Shirt("white", Material.FABRIC), stranger.getGeneralClothes());
        stranger.addGeneralClothes(new Trousers("grey", Season.SUMMER, trousersPatches), stranger.getGeneralClothes());
        stranger.addAccessories(new Hat("orange", Material.STRAW), stranger.getAccessories());
        stranger.addAccessories(new Glasses("red"), stranger.getAccessories());

        Conversation conv = new Conversation();
        conv.addPerson(kozlik);
        conv.addPerson(neznaika);
        conv.tellStory(kozlik);

        conv.addPerson(stranger);
        conv.tellJoke(stranger);

        conv.discuss(neznaika, kozlik, "stranger");
        conv.argue(neznaika, stranger);
        conv.makeDo(neznaika, kozlik, "leave");

        conv.removePerson(kozlik);

        conv.ask(stranger, neznaika);
        conv.showParticipants();
        conv.tellStory(stranger);

        conv.countPeopleWithState(State.INTERESTED);
        conv.countPeopleWithState(State.BORED);
        conv.countPeopleWithState(State.NEUTRAL);
        conv.countPeopleWithState(State.ANGRY);

    }

    static void startBedInteraction() {
        Known kozlik = new Known("Kozlik");
        Bed bed = kozlik.getBed(1);
        Bed.Bedsheet bedsheet = bed.getBedsheet(1);
        Bed.Pillow pillow = bed.getPillow(3);
        bedsheet.turnIntoAGhost(kozlik);
        Bed.Blanket blanket = bed.getBlanket(7);
        blanket.putAway(kozlik);
        kozlik.removeBed(bed);
    }
}
