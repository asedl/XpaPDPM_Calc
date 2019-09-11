/*
 * Home Health Grouper
 * Developer for the Center for Medicare and Medicaid Services CMS
 * by 3M Health Information Systems  for CMS Home Health.
 *
 * All code is provided as is.
 */

package com.mmm.cms.homehealth;

import com.mmm.cms.homehealth.proto.CaseMixAdjustmentItemIF;

/**
 * Holds a single Case Mix Adjustment row (or item) which includes a set
 * of values pertaining to the Scoring equations. 
 *
 * @author 3M Health Information Systems  for CMS Home Health
 */
public class CaseMixAdjustmentItem extends PointsScoringEquations implements CaseMixAdjustmentItemIF {

    /**
     * The case mix adjustment name/description
     */
    private String name;

	private int equations[];

	public CaseMixAdjustmentItem() {
		equations = new int[4];
	}
	
    /**
     * The case mix id, i.e. the row in the original case mix table
     */
    private int id;

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Set the value of id
     *
     * @param id new value of id
     */
    @Override
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    @Override
    public void setName(String Name) {
        this.name = Name;
    }

    /**
     * Get the value of equation1
     *
     * @return the value of equation1
     */
    @Override
    public int getEarly13AndUnder() {
        return equations[0];
    }

    /**
     * Set the value of equation1
     *
     * @param equation1 new value of equation1
     */
    @Override
    public void setEarly13AndUnder(int equation1) {
        this.equations[0] = equation1;
    }

    /**
     * Get the value of equation1
     *
     * @return the value of equation1
     */
    @Override
    public int getEarly14Plus() {
        return equations[1];
    }

    /**
     * Set the value of equation1
     *
     */
    @Override
    public void setEarly14Plus(int equation2) {
        this.equations[1] = equation2;
    }

    /**
     * Get the value of equation1
     *
     * @return the value of equation1
     */
    @Override
    public int getLater13AndUnder() {
        return equations[2];
    }

    /**
     * Set the value of equation1
     *
     */
    @Override
    public void setLater13AndUnder(int equation3) {
        this.equations[2] = equation3;
    }


    /**
     * Get the value of equation1
     *
     * @return the value of equation1
     */
    @Override
    public int getLater14Plus() {
        return equations[3];
    }

    /**
     * Set the value of equation1
     * @param equation4
     */
    @Override
    public void setLater14Plus(int equation4) {
        this.equations[3] = equation4;
    }

    /**
     * @param equationId
     * @return the case mix adjustment for the supplied equation id, or 0
     * if the id is not valid
     */
    @Override
    public int getAdjustment(int equationId) {
		int adjustment;
		
        switch (equationId) {
            case 1:
                adjustment = equations[0];
				break;
            case 2:
                adjustment = equations[1];
				break;
            case 3:
                adjustment = equations[2];
				break;
            case 4:
                adjustment = equations[3];
				break;
			default:
					adjustment = 0;
        }
        return adjustment;
    }

    /**
     * This sets the equation value.  The equation identifiers are 1 based,
     * as in 1 thru 4 inclusive
     *
     * @param equationId - 1 thru 4 inclusive, identifying the equation as
     * equation1, equation2, equation3, and equation4 respectfully.  The match
     * up with the common understanding of each equation for timing and
     * therapy visits as Early13AndUnder, Early14Plus, Later13AndUnder,
     * and Later14Plus, respectfully
     * @param value
     */
    @Override
    public void setAdjustment(int equationId, int value) {
        switch (equationId) {
            case 1:
                setEarly13AndUnder(value);
                break;
            case 2:
                setEarly14Plus(value);
                break;
            case 3:
                setLater13AndUnder(value);
                break;
            case 4:
                setLater14Plus(value);
                break;
			default:
        }
    }

    /**
     * Creates a hash code based on the Case Mix id.
     * @return
     */
    @Override
    public int hashCode() {
        return (int) id;
    }

	@Override
	public boolean equals(Object obj) {
		boolean isEquals = false;

		if (obj instanceof CaseMixAdjustmentItemIF) {
			final CaseMixAdjustmentItem other = (CaseMixAdjustmentItem) obj;
			if (this.id == other.id) {
				isEquals = true;
			}
		}
		return isEquals;
	}
}
