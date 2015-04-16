/**
 * 07/06/2011 13:02:30 Copyright (C) 2011 10Pines S.R.L.
 */
package com.tenpines.integration.bean2bean;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.impl.NaryFromNative;
import ar.com.tenpines.orm.api.HibernateOrm;
import ar.com.tenpines.orm.api.operations.basic.FindById;
import com.tenpines.commons.tos.PersistibleTo;
import convention.persistent.PersistentSupport;
import net.sf.kfgodel.bean2bean.Bean2Bean;
import net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;
import net.sf.kfgodel.dgarcia.lang.reflection.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Esta clase sabe como convertir de un TO a un objeto de dominio usando su ID para buscarlo en la
 * base
 *
 * @author D. García
 */
public class PersistibleTo2PersistentConverter implements SpecializedTypeConverter<PersistibleTo, PersistentSupport> {

    private HibernateOrm hibernate;

    private Bean2Bean bean2Bean;

    /**
     * @see net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter#convertTo(java.lang.reflect.Type,
     *      java.lang.Object, java.lang.annotation.Annotation[])
     */
    @Override
    public PersistentSupport convertTo(final Type expectedType, final PersistibleTo sourceObject,
                                        final Annotation[] contextAnnotations) throws CannotConvertException {
        Class<?> persistibleType;
        try {
            persistibleType = (Class<?>) expectedType;
        } catch (final ClassCastException e) {
            throw new CannotConvertException("El tipo destino no es una clase", sourceObject, expectedType, e);
        }
        if (!PersistentSupport.class.isAssignableFrom(persistibleType)) {
            throw new CannotConvertException("El tipo destino no es una subclase de " + PersistentSupport.class,
                    sourceObject, expectedType);
        }
        @SuppressWarnings("unchecked")
        final Class<PersistentSupport> persistibleSupportSubclass = (Class<PersistentSupport>) persistibleType;

        final Long persistibleId = sourceObject.getId();
        Nary<PersistentSupport> foundPersistent;
        if (persistibleId != null && persistibleId > 0) {
            // Como flex manda 0 por null, tenemos que tomar ese caso como null
            foundPersistent = hibernate.doWithSession((context)-> context.perform(FindById.create(persistibleSupportSubclass, persistibleId)));
        }else{
            // Si no hay id, entonces no existe entidad persistida
            foundPersistent = NaryFromNative.empty();
        }
        // Si no tenía id, o no lo encontramos en la base, creamos uno
        PersistentSupport persistent = foundPersistent
                .orElseGet(()-> ReflectionUtils.createInstance(persistibleSupportSubclass));

        // Copiamos el estado
        bean2Bean.copyPropertiesTo(persistent, sourceObject);
        return persistent;
    }

    public static PersistibleTo2PersistentConverter create(Bean2Bean b2b, HibernateOrm hibernate) {
        PersistibleTo2PersistentConverter converter = new PersistibleTo2PersistentConverter();
        converter.bean2Bean = b2b;
        converter.hibernate = hibernate;
        return converter;
    }

}