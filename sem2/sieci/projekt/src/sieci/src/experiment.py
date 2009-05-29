import ns3
import logging

class DeviceFactory:
    def installP2pDevices(self):
        p2p = ns3.PointToPointHelper()
        p2p.SetDeviceAttribute("DataRate", ns3.StringValue("500kbps"))
        p2p.SetChannelAttribute("Delay", ns3.StringValue("2ms"))
        self.devices = p2p.Install(self.nodes)
        self.logDevices()
        return self.devices
    
    def logDevices(self):
        logging.debug(str(self.devices.GetN()) + ' devices installed')
        for x in range(self.devices.GetN()):
            logging.debug('device(' + str(x) + ').address = ' + str(self.devices.Get(x).GetAddress()))
    
class InternetFactory:
    def assignAddr(self):
        addressHelper = ns3.Ipv4AddressHelper()
        addressHelper.SetBase(ns3.Ipv4Address(self.baseAddress), ns3.Ipv4Mask("255.255.255.0"))
        interfaces = addressHelper.Assign(self.devices);
        
        logging.debug(str(interfaces.GetN()) + ' addresses assigned')
        for x in range(interfaces.GetN()):
            logging.debug('interface(' + str(x) + ').address = ' + str(interfaces.GetAddress(x)))
        
        return interfaces
    
    def installInternetStack(self):
        stack = ns3.InternetStackHelper()
        stack.Install(self.nodes)
        
class AppFactory:
    def installTxApp(self):
        port = 9
        rxInetSocketAddress = ns3.InetSocketAddress(self.rxAddress, port)
        logging.debug('rxInetSocketAddress.ipv4 = ' + str(rxInetSocketAddress.GetIpv4()))
        logging.debug('rxInetSocketAddress.port = ' + str(rxInetSocketAddress.GetPort()))
        
        # install tx app
        # ns3.Config.SetDefault("ns3::OnOffApplication::PacketSize", ns3.UintegerValue(2000));
        # ns3.Config.SetDefault("ns3::OnOffApplication::DataRate", ns3.StringValue("60Mb/s"));
        
        txHelper = ns3.OnOffHelper("ns3::UdpSocketFactory",
                                   rxInetSocketAddress)
        
        txHelper.SetAttribute("OnTime",
                              ns3.RandomVariableValue(ns3.ConstantVariable(1)))
        txHelper.SetAttribute("OffTime",
                              ns3.RandomVariableValue(ns3.ConstantVariable(0)))
        
        txApp = txHelper.Install(self.txNode)
        txApp.Start(ns3.Seconds(self.txStart))
        txApp.Stop(ns3.Seconds(self.txStop))
        
        # install rx app
        rxHelper = ns3.PacketSinkHelper("ns3::UdpSocketFactory",
                                        rxInetSocketAddress)
        rxApp = rxHelper.Install(self.rxNode)
        rxApp.Start(ns3.Seconds(self.rxStart))
        rxApp.Stop(ns3.Seconds(self.rxStop))

def simulateSingleHop():
    nodes = ns3.NodeContainer()
    nodes.Create(2)

    devFactory = DeviceFactory()
    devFactory.nodes = nodes
    devices = devFactory.installP2pDevices()

    # internet stack
    internetFactory = InternetFactory()
    internetFactory.nodes = nodes
    internetFactory.installInternetStack()

    internetFactory.devices = devices
    internetFactory.baseAddress = "10.0.0.0"
    interfaces = internetFactory.assignAddr()

    # install applications
    appFactory = AppFactory()
    appFactory.txNode = nodes.Get(0)
    appFactory.rxNode = nodes.Get(1)
    appFactory.rxAddress = interfaces.GetAddress(1)
    appFactory.txStart = 10.0
    appFactory.txStop = 20.0
    appFactory.rxStart = 15.0
    appFactory.rxStop = 20.0
    appFactory.installTxApp()

    ns3.PointToPointHelper().EnablePcapAll('singleHop')

    ns3.Simulator.Stop(ns3.Seconds(30.0))
    ns3.Simulator.Run()
    ns3.Simulator.Destroy()

def simulateDualHop(olsr):
    nodes = ns3.NodeContainer()
    nodes.Create(3)
    
    nodes01 = ns3.NodeContainer()
    nodes01.Add(nodes.Get(0))
    nodes01.Add(nodes.Get(1))
    
    nodes12 = ns3.NodeContainer()
    nodes12.Add(nodes.Get(1))
    nodes12.Add(nodes.Get(2))
    
    # p2p devices
    devFactory = DeviceFactory()
    devFactory.nodes = nodes01
    devices01 = devFactory.installP2pDevices()
    
    devFactory.nodes = nodes12
    devices12 = devFactory.installP2pDevices()
    
    # internet stack
    internetFactory = InternetFactory()
    internetFactory.nodes = nodes
    internetFactory.installInternetStack()
    
    internetFactory.devices = devices01
    internetFactory.baseAddress = "10.0.1.0"
    interfaces01 = internetFactory.assignAddr()
    
    internetFactory.devices = devices12
    internetFactory.baseAddress = "10.0.2.0"
    interfaces12 = internetFactory.assignAddr()
    
    # install applications
    appFactory = AppFactory()
    appFactory.txNode = nodes01.Get(0)
    appFactory.rxNode = nodes12.Get(1)
    appFactory.rxAddress = interfaces12.GetAddress(1)
    appFactory.txStart = 80.0
    appFactory.txStop = 90.0
    appFactory.rxStart = 70.0
    appFactory.rxStop = 100.0
    appFactory.installTxApp()
    
    if (olsr):
        pcapName = "dualHopOlsr"
        olsr = ns3.OlsrHelper()
        olsr.InstallAll ();
    else:
        ns3.GlobalRouteManager.PopulateRoutingTables();
        pcapName = "dualHomeGlobalRoutes"
    
    ns3.PointToPointHelper().EnablePcapAll(pcapName)
    
    ns3.Simulator.Stop(ns3.Seconds(120.0))
    ns3.Simulator.Run()
    ns3.Simulator.Destroy()

def simulate():
    simulateSingleHop()
    simulateDualHop(olsr=False)
    simulateDualHop(olsr=True)
