package ar.com.tenpines.html5poc.components.transformer;

import ar.com.tenpines.html5poc.Application;
import ar.com.tenpines.html5poc.components.transformer.flavors.Identifiable2NumberConverter;
import ar.com.tenpines.html5poc.persistent.PersistibleSupport;
import ar.com.tenpines.orm.api.crud.Identifiable;
import net.sf.kfgodel.bean2bean.Bean2Bean;
import net.sf.kfgodel.bean2bean.conversion.DefaultTypeConverter;
import net.sf.kfgodel.bean2bean.conversion.TypeConverter;
import net.sf.kfgodel.bean2bean.instantiation.EmptyConstructorObjectFactory;

/**
 * This type implements the converter using bean2bean
 * Created by kfgodel on 05/04/15.
 */
public class B2BTransformer implements TypeTransformer {

    private Bean2Bean b2bManipulator;
    private TypeConverter b2bConverter;

    public static B2BTransformer create(Application application) {
        B2BTransformer converter = new B2BTransformer();
        converter.initialize(application);
        return converter;
    }

    private void initialize(Application application) {
        b2bConverter = DefaultTypeConverter.create();
        // Use niladic constructor to instantiate types
        b2bConverter.setObjectFactory(EmptyConstructorObjectFactory.create());
        // Register specialized converters (order is irrelevant)
        b2bConverter.registerSpecializedConverterFor(Identifiable.class, Long.class, Identifiable2NumberConverter.create());
        b2bConverter.registerSpecializedConverterFor(Number.class, PersistibleSupport.class, Identifiable2NumberConverter.create());
    }


}
