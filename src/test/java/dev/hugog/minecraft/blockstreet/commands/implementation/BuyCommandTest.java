package dev.hugog.minecraft.blockstreet.commands.implementation;


public class BuyCommandTest {

//    BuyCommand buyCommandSpy;
//    private PlayerMock playerMock;
//    private String[] args;
//
//    private CmdDependencyInjector cmdDependencyInjectorMock;
//
//    @BeforeEach
//    public void setUp() {
//
//        ServerMock serverMock = MockBukkit.mock();
//        BlockStreet pluginMock = MockBukkit.load(BlockStreet.class, true);
//
//        playerMock = serverMock.addPlayer("Hugo1307");
//        cmdDependencyInjectorMock = mock(CmdDependencyInjector.class);
//
//        when(cmdDependencyInjectorMock.getBlockStreet()).thenReturn(pluginMock);
//
//    }
//
//    @AfterEach
//    public void tearDown()  {
//        MockBukkit.unload();
//    }
//
//    @Test
//    @DisplayName("Player permissions check.")
//    public void testCommandPermissions() {
//
//        args = new String[] {"buy"};
//        buyCommandSpy = spy(new BuyCommand(playerMock, args, cmdDependencyInjectorMock));
//        buyCommandSpy.execute();
//
//        assertEquals("§l§aBlockStreet§6§l > §r§r§7You don't have enough permissions.", playerMock.nextMessage());
//
//    }
//
//    @Test
//    @DisplayName("No arguments check.")
//    public void testCommandWithoutArgs() {
//
//        playerMock.setOp(true);
//
//        args = new String[] {"buy"};
//        buyCommandSpy = spy(new BuyCommand(playerMock, args, cmdDependencyInjectorMock));
//        buyCommandSpy.execute();
//
//        assertEquals("§l§aBlockStreet§6§l > §r§r§7There are missing arguments.", playerMock.nextMessage());
//
//    }
//
//    @Test
//    @DisplayName("Insufficient arguments check.")
//    public void testCommandWithInsufficientArgs() {
//
//        playerMock.setOp(true);
//
//        args = new String[] {"buy", "1"};
//        buyCommandSpy = spy(new BuyCommand(playerMock, args, cmdDependencyInjectorMock));
//        buyCommandSpy.execute();
//
//        assertEquals("§l§aBlockStreet§6§l > §r§r§7There are missing arguments.", playerMock.nextMessage());
//
//    }
//
//    @Test
//    @DisplayName("All arguments check.")
//    public void testCommandWithArgs() {
//
//        playerMock.setOp(true);
//
//        Company spyCompany = spy(new Company(1)).load();
//
//        int companyStocks = spyCompany.getAvailableStocks();
//
//        args = new String[] {"buy", "1", "1"};
//        buyCommandSpy = spy(new BuyCommand(playerMock, args, cmdDependencyInjectorMock));
//        buyCommandSpy.execute();
//
//        Investor spyInvestor = spy(new Investor("Hugo1307"));
//
//        assertEquals("§l§aBlockStreet§6§l > §r§r§7You have bought 1 stocks.", playerMock.nextMessage());
//        assertTrue(spyInvestor.getInvestments().contains(new Investment(1, 1)));
//
//        if (spyCompany.getAvailableStocks() != -1)
//            assertEquals(companyStocks-1, spyCompany.getAvailableStocks());
//
//    }

}