/* -*-    Mode: C++; c-file-style: "gnu"; indent-tabs-mode:nil; -*- */
/*
 * Copyright (c) 2009 Drexel University
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation;
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.    See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA    02111-1307  USA
 *
 * Author: Joe Kopena <tjkopena@cs.drexel.edu>
 */

#include <fstream>
#include <iostream>
#include <string>

#include "ns3/core-module.h"
#include "ns3/simulator-module.h"
#include "ns3/common-module.h"
#include "ns3/node-module.h"
#include "ns3/helper-module.h"
#include "ns3/mobility-module.h"
#include "ns3/wifi-module.h"
#include "ns3/ipv4-address.h"
#include "ns3/gtk-config-store.h"
#include "ns3/config.h"
#include "ns3/olsr-header.h"

#include "ns3/decorator-frontend.h"

#include "wifi-trace-apps.h"
#include "packet-listener.h"

NS_LOG_COMPONENT_DEFINE ("wifi-project");

using namespace ns3;

void InitLogging() {
    LogComponentEnable("wifi-project", LOG_LEVEL_ALL);
    LogComponentEnable("packet-listener", LOG_LEVEL_ALL);
    LogComponentEnable("WifiTraceApps", LOG_LEVEL_ALL);
}

NetDeviceContainer InstallWifi(NodeContainer nodes) {
    NS_LOG_INFO("Installing WiFi and Internet stack.");

    WifiHelper wifi = WifiHelper::Default();
    wifi.SetMac("ns3::AdhocWifiMac");
    YansWifiPhyHelper wifiPhy = YansWifiPhyHelper::Default();
    YansWifiChannelHelper wifiChannel = YansWifiChannelHelper::Default();
    wifiPhy.SetChannel(wifiChannel.Create());
    NetDeviceContainer nodeDevices = wifi.Install(wifiPhy, nodes);
    wifiPhy.EnablePcapAll("wifi-project");
    return nodeDevices;
}

void InstallInternetStack(NodeContainer nodes, NetDeviceContainer nodeDevices) {
    // Install internet stack
    InternetStackHelper internet;
    internet.Install(nodes);
    Ipv4AddressHelper ipAddrs;
    ipAddrs.SetBase("192.168.0.0", "255.255.255.0");
    Ipv4InterfaceContainer ipInterfaces = ipAddrs.Assign(nodeDevices);

    for (uint i = 0; i < ipInterfaces.GetN(); i++) {
        Ipv4Address adr = ipInterfaces.GetAddress(i);
        std::ostringstream adrStream;
        adr.Print(adrStream);
        NS_LOG_INFO("ip of node " << i << ": " << adrStream.str());
    }
}

void InstallOlsr() {
    // initialize olsr routing
    OlsrHelper olsr = OlsrHelper();
    olsr.InstallAll();
    NS_LOG_INFO("olsr installed");
}

void ExperimentTwoNodes(int argc, char *argv[]) {
    NodeContainer nodes;
    nodes.Create(3);

    NetDeviceContainer netDevices = InstallWifi(nodes);
    InstallInternetStack(nodes, netDevices);
    InstallOlsr();

    // Setup physical layout
    MobilityHelper mobility;

    float distance = 100.0f;

    mobility.SetPositionAllocator("ns3::GridPositionAllocator", "MinX",
            DoubleValue(10), "MinY", DoubleValue(10), "DeltaX", DoubleValue(
                    distance), "DeltaY", DoubleValue(distance), "GridWidth",
            UintegerValue(nodes.GetN()), "LayoutType", StringValue("RowFirst"));
    mobility.SetMobilityModel("ns3::ConstantPositionMobilityModel");
    mobility.Install(nodes);

    // setup decorator
    DecoratorFrontend decorator;
    decorator.SetArena(Rectangle(0, (nodes.GetN() * distance) + 20, 0, 30));
    std::ofstream out("decorator.log");
    decorator.SetStream(&out);
    decorator.Init();

    // create sender and receiver application
    Ptr<Node> appSource = NodeList::GetNode(0);
    Ptr<Sender> sender = CreateObject<Sender> ();
    appSource->AddApplication(sender);
    sender->Start(Seconds(51));
    Config::Set("/NodeList/*/ApplicationList/*/$Sender/Destination",
            Ipv4AddressValue("192.168.0.3"));

    Ptr<Node> appSink = NodeList::GetNode(2);
    Ptr<Receiver> receiver = CreateObject<Receiver> ();
    appSink->AddApplication(receiver);
    receiver->Start(Seconds(50));

    PacketListener listener;
    listener.setNetDevices(netDevices);
    listener.setDecorator(decorator);

    Config::Connect("/NodeList/*/$ns3::Ipv4L3Protocol/Rx", MakeCallback(
            &PacketListener::RxCallback, &listener));
    Config::Connect("/NodeList/0/ApplicationList/0/$Sender/Tx", MakeCallback(
            &PacketListener::TxCallback, &listener));

    NS_LOG_INFO("Run Simulation.");

    Simulator::Stop(Seconds(120));
    Simulator::Run();
    Simulator::Destroy();

    out.close();
}

void ExperimentGrid(int argc, char *argv[]) {
    double distance = 120;
    int numNodes = 17;
    double duration = 480;

    CommandLine cmd;
    cmd.AddValue("nodes", "Number of nodes in simulation.", numNodes);
    cmd.AddValue("distance", "Distance between nodes.", distance);
    cmd.AddValue("duration", "How long the simulation should run.", duration);
    cmd.Parse(argc, argv);

    // Create nodes and network stacks
    NS_LOG_INFO("Creating nodes.");
    NodeContainer netNodes;
    netNodes.Create(numNodes - 1);
    NodeContainer movingNode;
    movingNode.Create(1);
    NodeContainer allNodes(netNodes, movingNode);

    NetDeviceContainer nodeDevices = InstallWifi(allNodes);
    InstallInternetStack(allNodes, nodeDevices);
    InstallOlsr();

    // Setup physical layout
    MobilityHelper mobility;

    numNodes -= 1;
    int width = sqrt(numNodes);
    double xbounds = width * distance;
    double ybounds = ceil((double) numNodes / (double) width) * distance;
    double min = distance / 2;

    NS_LOG_INFO("width " << width);
    NS_LOG_INFO("bounds " << xbounds << ", " << ybounds);
    NS_LOG_INFO("min " << min);

    mobility.SetPositionAllocator("ns3::GridPositionAllocator", "MinX",
            DoubleValue(min), "MinY", DoubleValue(min), "DeltaX", DoubleValue(
                    distance), "DeltaY", DoubleValue(distance), "GridWidth",
            UintegerValue(width), "LayoutType", StringValue("RowFirst"));

    mobility.SetMobilityModel("ns3::ConstantPositionMobilityModel");
    //mobility.SetMobilityModel("ns3::RandomWalk2dMobilityModel", "Bounds",
    //         RectangleValue(Rectangle(0, xbounds, 0, ybounds)));
    mobility.Install(netNodes);

    Ptr<ListPositionAllocator> positionAlloc = CreateObject<
            ListPositionAllocator> ();
    positionAlloc->Add(Vector(xbounds / 2, ybounds / 2, 0.0));
    mobility.SetPositionAllocator(positionAlloc);

    mobility.SetMobilityModel("ns3::RandomWalk2dMobilityModel", "Bounds",
            RectangleValue(Rectangle(0.0, xbounds, 0.0, ybounds)), "Distance",
            DoubleValue(300.0), "Speed", RandomVariableValue(UniformVariable(
                    5.0, 6.0)));

    mobility.Install(movingNode);

    // setup decorator
    DecoratorFrontend decorator;
    decorator.SetArena(Rectangle(0, xbounds, 0, ybounds));
    std::ofstream out("decorator.log");
    decorator.SetStream(&out);
    decorator.Init();

    // create sender and receiver application
    Ptr<Node> appSource = NodeList::GetNode(0);
    Ptr<Sender> sender = CreateObject<Sender> ();
    appSource->AddApplication(sender);
    sender->Start(Seconds(51));
    Config::Set("/NodeList/*/ApplicationList/*/$Sender/Destination",
            Ipv4AddressValue("192.168.0.17"));

    Ptr<Node> appSink = NodeList::GetNode(16);
    Ptr<Receiver> receiver = CreateObject<Receiver> ();
    appSink->AddApplication(receiver);
    receiver->Start(Seconds(50));

    PacketListener listener;
    listener.setNetDevices(nodeDevices);
    listener.setDecorator(decorator);

    Config::Connect("/NodeList/*/$ns3::Ipv4L3Protocol/Rx", MakeCallback(
            &PacketListener::RxCallback, &listener));
    Config::Connect("/NodeList/0/ApplicationList/0/$Sender/Tx", MakeCallback(
            &PacketListener::TxCallback, &listener));

    //------------------------------------------------------------
    //-- Run the simulation
    //--------------------------------------------
    NS_LOG_INFO("Run Simulation.");
    Simulator::Stop(Seconds(duration));
    Simulator::Run();
    Simulator::Destroy();

    out.close();
}

int main(int argc, char *argv[]) {
    InitLogging();

    // disable fragmentation
    Config::SetDefault("ns3::WifiRemoteStationManager::FragmentationThreshold",
            StringValue("2200"));
    Config::SetDefault("ns3::WifiRemoteStationManager::RtsCtsThreshold",
            StringValue("2200"));

    ExperimentTwoNodes(argc, argv);
    return 0;
}
