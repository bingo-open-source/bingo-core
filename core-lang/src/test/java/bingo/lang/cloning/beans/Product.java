package bingo.lang.cloning.beans;

/**
 * @author kostantinos.kougios
 *
 * 30 Nov 2009
 */
public class Product
{
	private final int		id;
	private final String	sku;
	private final String	title;
	private Category category;

	public Product(final int id, final String sku, final String title)
	{
		this.id = id;
		this.sku = sku;
		this.title = title;
	}

	public int getId()
	{
		return id;
	}

	public String getSku()
	{
		return sku;
	}

	public String getTitle()
	{
		return title;
	}

	@Override
	public String toString()
	{
		return "product:" + sku;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj instanceof Product)
		{
			final Product p = (Product) obj;
			return p.getSku().equals(sku);
		}
		return super.equals(obj);
	}

	public final Category getCategory() {
    	return category;
    }

	public final void setCategory(Category category) {
    	this.category = category;
    }
}
