package fi.nls.oskari.control.view.modifier.bundle;

import fi.nls.oskari.annotation.OskariViewModifier;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.util.JSONHelper;
import fi.nls.oskari.util.PropertyUtil;
import fi.nls.oskari.view.modifier.ModifierException;
import fi.nls.oskari.view.modifier.ModifierParams;
import org.json.JSONObject;

@OskariViewModifier("myplacesimport")
public class MyplacesimportHandler extends BundleHandler {
    private static final Logger log = LogFactory.getLogger(MyplacesimportHandler.class);

    public boolean modifyBundle(final ModifierParams params) throws ModifierException {
        final JSONObject config = getBundleConfig(params.getConfig());
        JSONHelper.putValue(config, "maxFileSizeMb", PropertyUtil.get("userlayer.max.filesize.mb"));
        return false;
    }
}