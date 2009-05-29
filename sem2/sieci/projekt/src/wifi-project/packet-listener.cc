#include "packet-listener.h"

#include "ns3/log.h"
#include "ns3/ipv4.h"
#include "ns3/nstime.h"
#include "wifi-trace-apps.h"

using namespace ns3;

NS_LOG_COMPONENT_DEFINE ("packet-listener");

PacketListener::PacketListener() :
    netDevices(NULL), decorator(NULL) {
    NS_LOG_FUNCTION_NOARGS();
}

void PacketListener::setNetDevices(NetDeviceContainer& netDevices) {
    this->netDevices = &netDevices;
}

void PacketListener::setDecorator(DecoratorFrontend& decorator) {
    this->decorator = &decorator;
}

void PacketListener::add(uint32_t uid, uint node) {
    this->packets[uid].push_back(node);
    NS_LOG_DEBUG("packet-listener:add packet " << uid << " got node " << node
            << " (" << this->packets[uid].size() << " total nodes)");
}

void PacketListener::tokenize(const std::string &str,
        std::vector<std::string> &tokens, const std::string &delimiters = "/") {
    // Skip delimiters at beginning.
    std::string::size_type lastPos = str.find_first_not_of(delimiters, 0);
    // Find first "non-delimiter".
    std::string::size_type pos = str.find_first_of(delimiters, lastPos);

    while (std::string::npos != pos || std::string::npos != lastPos) {
        // Found a token, add it to the vector.
        tokens.push_back(str.substr(lastPos, pos - lastPos));
        // Skip delimiters.  Note the "not_of"
        lastPos = str.find_first_not_of(delimiters, pos);
        // Find next "non-delimiter"
        pos = str.find_first_of(delimiters, lastPos);
    }
}

template<class T>
bool PacketListener::fromString(T& t, const std::string& s,
        std::ios_base& (*f)(std::ios_base&)) {
    std::istringstream iss(s);
    return !(iss >> f >> t).fail();
}

uint PacketListener::getNode(std::string &context) {
    std::vector<std::string> list;
    this->tokenize(context, list, "/");

    uint node;
    this->fromString(node, list[1], std::dec);
    return node;
}

void PacketListener::TxCallback(std::string context, Ptr<const Packet> packet) {
    uint node = this->getNode(context);
    this->add(packet->GetUid(), node);

    NS_LOG_INFO("packet-listener:TxCallback node=" << node << ", packetUid="
            << packet->GetUid());
}

void PacketListener::killLink(Ptr<Node> src, Ipv4Address ipv4Address,
        std::string state) {
    this->decorator->RemoveLinkState(src, ipv4Address, state);
}

void PacketListener::createLink(uint srcNode, uint targetNode) {
    Ptr<Node> src = NodeList::GetNode(srcNode);
    NS_ASSERT(src != 0);

    Ptr<NetDevice> dvc = this->netDevices->Get(targetNode);
    NS_ASSERT(dvc != 0);

    Ptr<Node> target = dvc->GetNode();
    NS_ASSERT(target != 0);

    Ptr<Ipv4> ipv4 = target->GetObject<Ipv4> ();
    NS_ASSERT(ipv4 != 0);

    uint32_t id = ipv4->FindInterfaceForDevice(dvc);
    Ipv4Address ipv4Address = ipv4->GetAddress(id);

    NS_LOG_INFO("creating link for " << ipv4Address);

    std::string state = "fancy";

    this->decorator->AddLinkState(src, ipv4Address, state);

    Simulator::Schedule(Seconds(3.0), &PacketListener::killLink, this, src,
            ipv4Address, state);
}

void PacketListener::RxCallback(std::string context, Ptr<const Packet> packet,
        uint32_t interfaceIndex) {
    uint node = this->getNode(context);

    Nodes *n = &this->packets[packet->GetUid()];

    if (n->size() > 0) {
        n->push_back(node);
        uint srcNode = (*n)[n->size() - 2];
        NS_LOG_INFO("packet-listener:RxCallback node=" << node
                << ", packetUid=" << packet->GetUid() << " nodes=" << n->size()
                << ", generating link between " << srcNode << " and " << node);

        this->createLink(srcNode, node);
    }
}
