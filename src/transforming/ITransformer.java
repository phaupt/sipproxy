package transforming;

import proxy.UDPDatagram;

public interface ITransformer {

    public void transformMessage( UDPDatagram packet );

}
