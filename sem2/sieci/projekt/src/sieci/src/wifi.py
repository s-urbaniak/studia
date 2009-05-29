import logging
import ns3

def installGrid(wifiNodes):
    logging.debug("installing grid")
    m = ns3.MobilityHelper()
    
    m.SetPositionAllocator(type="ns3::GridPositionAllocator"
                           ,n1="MinX", v1=ns3.DoubleValue(0.0)
                           ,n2="MinY", v2=ns3.DoubleValue(0.0)
                           ,n3="DeltaX", v3=ns3.DoubleValue(150.0)
                           ,n4="DeltaY", v4=ns3.DoubleValue(150.0)
                           )
    
    m.Install(wifiNodes)
    
def wifiSimulation():
    logging.debug("wifi Simulation start")

    # create nodes
    wifiNodes = ns3.NodeContainer()
    wifiNodes.Create(9)
    assert wifiNodes
    logging.debug(str(wifiNodes.GetN()) + " nodes created")

    wifiChannel = ns3.YansWifiChannelHelper.Default()
    assert wifiChannel
    logging.debug("wifi channel install")

    wifiPhy = ns3.YansWifiPhyHelper.Default()
    assert wifiPhy
    wifiPhy.EnablePcapAll("wifi")
    logging.debug("wifi phy install")

    installGrid(wifiNodes)
