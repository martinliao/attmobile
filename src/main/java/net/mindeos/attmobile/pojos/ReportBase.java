package net.mindeos.attmobile.pojos;

/**
 * Base class for the POJOs representing ReportItems (common for AttControl and AttHome).
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public interface ReportBase
{
    /**
     * Is this item a header.
     *
     * @return the boolean indicating whether this report item is a header or not
     */
    public boolean isHeader();
}
