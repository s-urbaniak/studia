#include <map>
#include <list>

#include "ns3/core-module.h"
#include "ns3/node-list.h"
#include "ns3/packet.h"
#include "ns3/ptr.h"
#include "ns3/decorator-frontend.h"
#include "ns3/net-device-container.h"

using namespace ns3;

typedef std::vector<uint32_t> Nodes;
typedef std::map<uint32_t, Nodes> PacketMap;

class PacketListener {
public:
    PacketListener();
    void add(uint32_t uid, uint node);
    void RxCallback(std::string context, Ptr<const Packet> packet,
            uint32_t interfaceIndex);
    void TxCallback(std::string context, Ptr<const Packet> packet);
    void setNetDevices(NetDeviceContainer &netDevices);
    void setDecorator(DecoratorFrontend &decorator);
    void createLink(uint sourceNode, uint targetNode);
    void killLink(Ptr<Node> src, Ipv4Address ipv4Address, std::string state);

private:
    void tokenize(const std::string &str, std::vector<std::string> &tokens,
            const std::string &delimiters);

    template<class T>
    bool fromString(T& t, const std::string& s, std::ios_base& (*f)(
            std::ios_base&));

    uint getNode(std::string& context);

    PacketMap packets;
    NetDeviceContainer *netDevices;
    DecoratorFrontend *decorator;
};
